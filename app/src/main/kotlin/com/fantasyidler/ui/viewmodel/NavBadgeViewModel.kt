package com.fantasyidler.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasyidler.data.model.PlayerFlags
import com.fantasyidler.repository.DailyQuestRepository
import com.fantasyidler.repository.GameDataRepository
import com.fantasyidler.repository.PlayerRepository
import com.fantasyidler.repository.QuestRepository
import com.fantasyidler.repository.WeeklyQuestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class NavBadgeViewModel @Inject constructor(
    questRepo: QuestRepository,
    gameData: GameDataRepository,
    playerRepo: PlayerRepository,
    dailyQuestRepo: DailyQuestRepository,
    weeklyQuestRepo: WeeklyQuestRepository,
    private val json: Json,
) : ViewModel() {

    val questsClaimableCount: StateFlow<Int> = combine(
        questRepo.observeProgress(),
        playerRepo.playerFlow,
    ) { progressList, player ->
        val progressMap = progressList.associateBy { it.questId }
        val regularCount = gameData.quests.count { (id, quest) ->
            val prog = progressMap[id]
            if (prog?.completed == true) return@count false
            val prereq = quest.requiresPrevious
            val prereqDone = prereq == null || progressMap[prereq]?.completed == true
            prereqDone && (prog?.progress ?: 0) >= quest.amount
        }
        if (player == null) return@combine regularCount
        val flags: PlayerFlags = json.decodeFromString(player.flags)
        val dailyCount = dailyQuestRepo.getActiveDailyQuests(flags)
            .count { it.progress >= it.template.amount && !it.claimed }
        val weeklyCount = weeklyQuestRepo.getActiveWeeklyQuests(flags)
            .count { it.progress >= it.template.amount && !it.claimed }
        regularCount + dailyCount + weeklyCount
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
}
