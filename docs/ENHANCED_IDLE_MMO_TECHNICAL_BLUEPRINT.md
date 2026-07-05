# Enhanced Idle MMO Technical Blueprint

Repository: `dylmarriner/IdleFantasy`  
Target platform: Android  
Base architecture: Kotlin, Jetpack Compose, Material 3, Room SQLite, WorkManager, MVVM, Repository pattern, Hilt, kotlinx.serialization  
Design direction: Melvor Idle-style offline progression + RuneScape-style skills and training choices + World of Warcraft-style races, classes, specs, talents, dungeons, raids, guilds, and boss roles.

---

## 0. Purpose

This document is the execution-ready technical blueprint for evolving the existing Idle Fantasy Android codebase into a deeper idle fantasy MMORPG-style RPG.

The goal is not to throw away the current project. The goal is to enhance the existing offline-first idle RPG foundation with persistent equipment, better combat, more skills, proper archery and magic, talent trees, races, classes, specializations, stronger gear progression, and future multiplayer hooks.

The first priority is to make the current game feel more like:

- Melvor Idle: long-term idle skilling, combat, gear, offline progress, mastery-style growth.
- RuneScape: many skills, independent combat training, equipment requirements, gathering and crafting economy.
- World of Warcraft: race/class/spec identity, talent trees, dungeon roles, gear builds, boss mechanics.

This blueprint is written so a developer can create branches, implement migrations, build screens, add tests, and check off work phase by phase.

---

## 1. Current Base Summary

The existing project already has a useful foundation:

- Android app.
- Kotlin codebase.
- Jetpack Compose UI.
- Material 3.
- Room SQLite local database.
- WorkManager for background/idle progress.
- MVVM and Repository pattern.
- Hilt dependency injection.
- kotlinx.serialization.
- Offline-first gameplay.
- No account requirement.
- No internet requirement.
- No Google Play Services dependency.
- F-Droid-compatible direction.
- 23 skills documented in the README.
- 20 dungeons documented in the README.
- Existing Melee, Ranged, and Magic combat style selection.
- Existing guild-style progression, currently skill/progression guilds rather than real multiplayer player guilds.
- Existing Church blessings.
- Existing Slayer system.
- Existing Inn/workers/pets systems.
- Existing Carnival and minigames.

The repo should remain offline-first while new systems are added. Multiplayer and server-authoritative systems should be added later as optional extensions, not as an immediate destructive rewrite.

---

## 2. Product Direction

### 2.1 New Product Identity

Working direction:

`Idle Fantasy: Reforged`

Alternative names:

- Ashen Realms
- Idle Realms Reforged
- Runebound Idle
- Guilds of Ashfall
- Hollow Crown Idle

The codebase can keep the current name during technical work. Rebrand later after the foundations are stable.

### 2.2 Core Player Promise

The player creates a fantasy character, chooses a race and class, equips persistent gear, trains many skills, specializes through talents, clears dungeons, gathers resources, crafts upgrades, and progresses while offline.

The player should feel long-term agency through:

- What skill they train.
- What combat style they use.
- Whether melee XP goes to Attack, Strength, Defense, or balanced training.
- What class and specialization they choose.
- What gear stays equipped.
- What talents shape their build.
- What profession path supports their combat build.
- What dungeon/boss they prepare for.

---

## 3. Non-Negotiable Design Corrections

### 3.1 Persistent Equipment

Equipped gear must stay equipped until the player replaces it or removes it.

Gear must not disappear, unequip after a session, or behave like temporary session gear unless the item is explicitly a consumable or timed buff.

Correct rule:

```text
Equip item -> item remains in equipment slot -> item modifies stats for all relevant gameplay -> item remains until replaced/removed.
```

### 3.2 Combat Style and Training Focus Must Be Separate

Current concept to preserve:

```text
Combat style = Melee / Ranged / Magic
```

New required concept:

```text
Training focus = which combat skill receives most XP and what tactical bias is applied.
```

For melee:

```text
Accurate   -> trains Attack
Aggressive -> trains Strength
Defensive  -> trains Defense
Controlled -> trains Attack, Strength, Defense evenly
```

This gives RuneScape-style combat training control.

### 3.3 Classes Must Not Kill Skill Freedom

The game should not become fully class-locked like a traditional MMO.

Correct model:

```text
RuneScape skill freedom
+ Melvor idle depth
+ WoW-style class/spec identity
```

A Warrior can still fish. A Mage can still mine. A Ranger can still cook. Classes define combat identity and bonuses, not total account restrictions.

### 3.4 Offline Progress Must Remain Central

All systems must support idle/offline operation.

Any new combat, skill, dungeon, talent, or class mechanic must be calculable without requiring real-time input.

---

## 4. Development Phases

### Phase 1: Persistent Equipment Foundation

Branch:

```bash
git checkout -b feature/persistent-equipment
```

Goal:

Make gear persistent, slot-based, requirement-checked, and visible in the character screen.

Deliverables:

- EquipmentSlot enum.
- EquipmentLoadout entity.
- Equip item use case.
- Unequip item use case.
- Replace item flow.
- Requirements validation.
- Equipment UI screen.
- Stat recalculation pipeline.
- Room migration.
- Tests.

Acceptance criteria:

- Gear remains equipped after app restart.
- Gear remains equipped after dungeon completion.
- Gear remains equipped after offline progress claim.
- Replacing an item returns the old item to inventory.
- Removing an item returns it to inventory.
- Items cannot be equipped if requirements are not met.
- Equipped items contribute to stat calculation.

### Phase 2: Combat Training Focus

Branch:

```bash
git checkout -b feature/combat-training-focus
```

Goal:

Allow the player to select Attack, Strength, Defense, or Controlled training when using Melee.

Deliverables:

- CombatTrainingFocus enum.
- XP split table.
- Focus combat modifiers.
- Dungeon/combat setup selector.
- Combat preview panel.
- Combat result XP breakdown.
- Room migration if active sessions store combat focus.
- Tests.

Acceptance criteria:

- Player can select Accurate, Aggressive, Defensive, or Controlled before starting melee combat.
- XP is split according to selected focus.
- Combat results show exact XP gained per skill.
- Focus applies small tactical modifiers.
- Offline combat uses the stored focus.

### Phase 3: Better Archery and Magic

Branch:

```bash
git checkout -b feature/archery-magic-combat
```

Goal:

Make Ranged/Archery and Magic feel like real combat systems, not just alternate damage labels.

Deliverables:

- Rename or alias Ranged as Archery in UI if desired.
- Add ranged weapon categories.
- Add ammo slot and ammo consumption rules.
- Add spell schools.
- Add rune/resource costs.
- Add magic training focus.
- Add enemy weaknesses/resistances.
- Add damage type system.
- Tests.

Acceptance criteria:

- Bows/crossbows use Archery/Ranged scaling.
- Ammo is persistent and consumed correctly.
- Magic uses spells, schools, and resource costs.
- Enemies can be weak/resistant to damage types.
- Combat preview shows expected style effectiveness.

### Phase 4: Race, Class, and Specialization

Branch:

```bash
git checkout -b feature/races-classes-specializations
```

Goal:

Add character identity and build direction without blocking skilling freedom.

Deliverables:

- CharacterRace enum.
- CharacterClass enum.
- CharacterSpecialization enum.
- CharacterBuild entity.
- Race passive system.
- Class passive system.
- Spec passive system.
- Character creation screen.
- Character overview screen.
- Migration for existing players using default Human Adventurer/Warrior fallback.
- Tests.

Acceptance criteria:

- New players choose race/class/spec.
- Existing players are migrated safely.
- Race/class/spec bonuses affect derived stats.
- Race/class/spec are persisted.
- Player can still train non-class skills.

### Phase 5: Talent Trees

Branch:

```bash
git checkout -b feature/talent-trees
```

Goal:

Add class and specialization talent trees with passive build choices.

Deliverables:

- TalentTree data model.
- TalentNode data model.
- TalentEffect model.
- PlayerTalentRank entity.
- Talent points calculation.
- Talent screen.
- Spend point use case.
- Respec use case.
- Talent stat integration.
- Tests.

Acceptance criteria:

- Player earns talent points from levels.
- Player can spend points on valid nodes.
- Locked nodes cannot be selected.
- Talent effects modify gameplay.
- Respec resets talents safely.
- Talents persist after app restart.

### Phase 6: Expanded Skills

Branch:

```bash
git checkout -b feature/expanded-skills
```

Goal:

Add new skills in controlled batches.

First batch:

- Evasion.
- Herbalism.
- Hunting.
- Leatherworking.
- Tailoring.
- Enchanting.
- Engineering.
- Leadership.

Deliverables:

- Skill enum additions.
- Skill metadata.
- XP tables.
- Actions for each new skill.
- Resources produced.
- Recipes consuming resources.
- UI display groups.
- Offline progress support.
- Tests.

Acceptance criteria:

- Each new skill can be trained.
- Each new skill has at least 10 progression actions or recipes.
- Each new skill connects to combat, gear, food, potions, dungeons, or future guild/world boss systems.
- Existing saves migrate safely.

---

## 5. Persistent Equipment Technical Specification

### 5.1 Equipment Slots

Add:

```kotlin
enum class EquipmentSlot {
    HEAD,
    NECK,
    SHOULDERS,
    CHEST,
    HANDS,
    LEGS,
    FEET,
    MAIN_HAND,
    OFF_HAND,
    RING_1,
    RING_2,
    TRINKET_1,
    TRINKET_2,
    CAPE,
    AMMO,
    TOOL
}
```

Initial MVP can support:

- Head
- Chest
- Legs
- Feet
- Main hand
- Off hand
- Ring 1
- Ring 2
- Ammo
- Tool

Shoulders, trinkets, cape, and neck can be added later.

### 5.2 Equipment Loadout Entity

Add Room entity:

```kotlin
@Entity(tableName = "equipment_loadouts")
data class EquipmentLoadoutEntity(
    @PrimaryKey val id: String,
    val playerId: String,
    val name: String,
    val isActive: Boolean,
    val headItemInstanceId: String? = null,
    val neckItemInstanceId: String? = null,
    val shouldersItemInstanceId: String? = null,
    val chestItemInstanceId: String? = null,
    val handsItemInstanceId: String? = null,
    val legsItemInstanceId: String? = null,
    val feetItemInstanceId: String? = null,
    val mainHandItemInstanceId: String? = null,
    val offHandItemInstanceId: String? = null,
    val ringOneItemInstanceId: String? = null,
    val ringTwoItemInstanceId: String? = null,
    val trinketOneItemInstanceId: String? = null,
    val trinketTwoItemInstanceId: String? = null,
    val capeItemInstanceId: String? = null,
    val ammoItemInstanceId: String? = null,
    val toolItemInstanceId: String? = null,
    val updatedAt: Long
)
```

### 5.3 Inventory Item State

Item instances should track whether they are equipped.

Preferred approach:

```kotlin
enum class ItemLocation {
    INVENTORY,
    EQUIPPED,
    BANK,
    CONSUMED,
    DELETED
}
```

Add to item instance entity if not present:

```kotlin
val location: ItemLocation
val equippedLoadoutId: String?
val equippedSlot: EquipmentSlot?
```

Rules:

- An item in `EQUIPPED` location cannot also appear as a normal inventory stack unless it is stackable ammo.
- Equipment actions must be transactional.
- Replacing equipped gear moves old item back to `INVENTORY`.
- Unequipping moves item back to `INVENTORY`.
- Deleting equipped items should be blocked unless unequipped first.

### 5.4 Equipment Requirements

Add:

```kotlin
data class ItemRequirement(
    val skill: SkillType,
    val level: Int
)
```

Add to item metadata:

```kotlin
data class EquipmentMetadata(
    val slot: EquipmentSlot,
    val allowedClasses: Set<CharacterClass> = emptySet(),
    val allowedRaces: Set<CharacterRace> = emptySet(),
    val requirements: List<ItemRequirement> = emptyList(),
    val weaponType: WeaponType? = null,
    val armorType: ArmorType? = null,
    val damageTypes: Set<DamageType> = emptySet()
)
```

Validation rules:

- If requirements are not met, item cannot be equipped.
- If allowedClasses is empty, all classes can use item.
- If allowedRaces is empty, all races can use item.
- Ammo requires a compatible ranged weapon.
- Off-hand item cannot be equipped if main-hand weapon is two-handed.
- Tool slot should not affect combat unless the tool explicitly has combat stats.

### 5.5 Equip Use Case

```kotlin
class EquipItemUseCase @Inject constructor(
    private val itemRepository: ItemRepository,
    private val equipmentRepository: EquipmentRepository,
    private val characterRepository: CharacterRepository,
    private val statCalculator: StatCalculator
) {
    suspend operator fun invoke(
        playerId: String,
        itemInstanceId: String,
        targetSlot: EquipmentSlot
    ): EquipResult {
        // 1. Load player build and skills.
        // 2. Load item instance and metadata.
        // 3. Validate item is owned by player.
        // 4. Validate item can occupy target slot.
        // 5. Validate requirements.
        // 6. In transaction:
        //    a. Move current equipped item in slot back to inventory.
        //    b. Move selected item to EQUIPPED.
        //    c. Update active loadout slot.
        // 7. Recalculate derived stats.
        // 8. Return result for UI.
    }
}
```

### 5.6 Unequip Use Case

```kotlin
class UnequipItemUseCase @Inject constructor(
    private val equipmentRepository: EquipmentRepository,
    private val itemRepository: ItemRepository,
    private val statCalculator: StatCalculator
) {
    suspend operator fun invoke(
        playerId: String,
        slot: EquipmentSlot
    ): UnequipResult {
        // 1. Load active loadout.
        // 2. Find equipped item in slot.
        // 3. If empty, return no-op.
        // 4. In transaction:
        //    a. Clear loadout slot.
        //    b. Move item to inventory.
        // 5. Recalculate stats.
    }
}
```

### 5.7 Equipment Screen

Screen sections:

- Character summary.
- Gear slots grid.
- Stat summary.
- Equipped item details.
- Replace button.
- Unequip button.
- Requirements warning.

Slot card should show:

- Slot name.
- Item icon.
- Item rarity.
- Main stat summary.
- Missing item indicator.

### 5.8 Persistent Equipment Tests

Unit tests:

- `equipItem_persistsInLoadout`.
- `equipItem_replacesExistingItem`.
- `unequipItem_returnsItemToInventory`.
- `equipItem_rejectsMissingRequirement`.
- `equipTwoHandedWeapon_clearsOffHand`.
- `equipAmmo_rejectsWithoutCompatibleWeapon`.
- `equippedGear_appliesToDerivedStats`.

Integration tests:

- Equip item, restart app, item still equipped.
- Equip item, run dungeon, item still equipped.
- Equip item, claim offline progress, item still equipped.

---

## 6. Combat Training Focus Technical Specification

### 6.1 Combat Style

Keep existing style concept:

```kotlin
enum class CombatStyle {
    MELEE,
    RANGED,
    MAGIC
}
```

Optional future extension:

```kotlin
enum class CombatStyle {
    MELEE,
    ARCHERY,
    MAGIC,
    PRAYER,
    NECROMANCY,
    SUMMONING
}
```

For now, preserve compatibility and only rename display text if needed.

### 6.2 Combat Training Focus

Add:

```kotlin
enum class CombatTrainingFocus {
    ACCURATE,
    AGGRESSIVE,
    DEFENSIVE,
    CONTROLLED,
    PRECISION,
    RAPID,
    HEAVY_SHOT,
    EVASIVE,
    DESTRUCTION,
    WARDING,
    RITUAL
}
```

MVP can use a smaller enum:

```kotlin
enum class CombatTrainingFocus {
    ATTACK,
    STRENGTH,
    DEFENSE,
    BALANCED
}
```

Recommended implementation:

Use broader enum now to avoid migrations later, but only expose valid options per combat style.

### 6.3 Valid Focus Options

Melee:

| UI Label | Internal | Main XP | Effect |
|---|---|---|---|
| Accurate | ACCURATE | Attack | +Accuracy, -Max Hit |
| Aggressive | AGGRESSIVE | Strength | +Max Hit, -Accuracy |
| Defensive | DEFENSIVE | Defense | +Damage Reduction, -Damage |
| Controlled | CONTROLLED | Attack/Strength/Defense | Balanced |

Archery/Ranged:

| UI Label | Internal | Main XP | Effect |
|---|---|---|---|
| Precision | PRECISION | Archery/Ranged | +Accuracy |
| Rapid | RAPID | Archery/Ranged | +Attack speed, -Max Hit |
| Heavy Shot | HEAVY_SHOT | Archery/Ranged | +Max Hit, slower |
| Evasive | EVASIVE | Archery/Ranged + Defense/Evasion | +Dodge |

Magic:

| UI Label | Internal | Main XP | Effect |
|---|---|---|---|
| Destruction | DESTRUCTION | Magic | +Spell damage |
| Warding | WARDING | Magic + Defense | +Resistance |
| Ritual | RITUAL | Magic + Prayer | +Buff duration |
| Controlled | CONTROLLED | Magic/Defense/Hitpoints | Balanced |

### 6.4 XP Split Tables

Melee XP split:

```kotlin
fun meleeXpSplit(focus: CombatTrainingFocus): Map<SkillType, Double> {
    return when (focus) {
        CombatTrainingFocus.ACCURATE -> mapOf(
            SkillType.ATTACK to 0.70,
            SkillType.STRENGTH to 0.10,
            SkillType.DEFENSE to 0.05,
            SkillType.HITPOINTS to 0.15
        )
        CombatTrainingFocus.AGGRESSIVE -> mapOf(
            SkillType.ATTACK to 0.10,
            SkillType.STRENGTH to 0.70,
            SkillType.DEFENSE to 0.05,
            SkillType.HITPOINTS to 0.15
        )
        CombatTrainingFocus.DEFENSIVE -> mapOf(
            SkillType.ATTACK to 0.10,
            SkillType.STRENGTH to 0.05,
            SkillType.DEFENSE to 0.70,
            SkillType.HITPOINTS to 0.15
        )
        CombatTrainingFocus.CONTROLLED -> mapOf(
            SkillType.ATTACK to 0.28,
            SkillType.STRENGTH to 0.28,
            SkillType.DEFENSE to 0.28,
            SkillType.HITPOINTS to 0.16
        )
        else -> meleeXpSplit(CombatTrainingFocus.CONTROLLED)
    }
}
```

Archery XP split:

```kotlin
fun archeryXpSplit(focus: CombatTrainingFocus): Map<SkillType, Double> {
    return when (focus) {
        CombatTrainingFocus.PRECISION -> mapOf(
            SkillType.RANGED to 0.75,
            SkillType.HITPOINTS to 0.15,
            SkillType.EVASION to 0.10
        )
        CombatTrainingFocus.RAPID -> mapOf(
            SkillType.RANGED to 0.80,
            SkillType.HITPOINTS to 0.15,
            SkillType.EVASION to 0.05
        )
        CombatTrainingFocus.HEAVY_SHOT -> mapOf(
            SkillType.RANGED to 0.70,
            SkillType.STRENGTH to 0.10,
            SkillType.HITPOINTS to 0.15,
            SkillType.EVASION to 0.05
        )
        CombatTrainingFocus.EVASIVE -> mapOf(
            SkillType.RANGED to 0.55,
            SkillType.DEFENSE to 0.15,
            SkillType.EVASION to 0.15,
            SkillType.HITPOINTS to 0.15
        )
        else -> mapOf(
            SkillType.RANGED to 0.75,
            SkillType.HITPOINTS to 0.15,
            SkillType.EVASION to 0.10
        )
    }
}
```

Magic XP split:

```kotlin
fun magicXpSplit(focus: CombatTrainingFocus): Map<SkillType, Double> {
    return when (focus) {
        CombatTrainingFocus.DESTRUCTION -> mapOf(
            SkillType.MAGIC to 0.80,
            SkillType.HITPOINTS to 0.15,
            SkillType.RUNECRAFTING to 0.05
        )
        CombatTrainingFocus.WARDING -> mapOf(
            SkillType.MAGIC to 0.55,
            SkillType.DEFENSE to 0.20,
            SkillType.HITPOINTS to 0.15,
            SkillType.PRAYER to 0.10
        )
        CombatTrainingFocus.RITUAL -> mapOf(
            SkillType.MAGIC to 0.55,
            SkillType.PRAYER to 0.25,
            SkillType.HITPOINTS to 0.10,
            SkillType.RUNECRAFTING to 0.10
        )
        CombatTrainingFocus.CONTROLLED -> mapOf(
            SkillType.MAGIC to 0.60,
            SkillType.DEFENSE to 0.15,
            SkillType.HITPOINTS to 0.15,
            SkillType.PRAYER to 0.10
        )
        else -> mapOf(
            SkillType.MAGIC to 0.80,
            SkillType.HITPOINTS to 0.15,
            SkillType.RUNECRAFTING to 0.05
        )
    }
}
```

If Evasion does not exist yet, replace Evasion XP with Defense until Phase 6.

### 6.5 Focus Modifiers

```kotlin
data class CombatFocusModifier(
    val accuracyMultiplier: Double = 1.0,
    val damageMultiplier: Double = 1.0,
    val defenseMultiplier: Double = 1.0,
    val attackSpeedMultiplier: Double = 1.0,
    val dodgeMultiplier: Double = 1.0,
    val resistanceMultiplier: Double = 1.0,
    val buffDurationMultiplier: Double = 1.0
)
```

```kotlin
fun getFocusModifier(focus: CombatTrainingFocus): CombatFocusModifier {
    return when (focus) {
        CombatTrainingFocus.ACCURATE -> CombatFocusModifier(
            accuracyMultiplier = 1.08,
            damageMultiplier = 0.97
        )
        CombatTrainingFocus.AGGRESSIVE -> CombatFocusModifier(
            accuracyMultiplier = 0.97,
            damageMultiplier = 1.08
        )
        CombatTrainingFocus.DEFENSIVE -> CombatFocusModifier(
            damageMultiplier = 0.95,
            defenseMultiplier = 1.08
        )
        CombatTrainingFocus.CONTROLLED -> CombatFocusModifier()
        CombatTrainingFocus.PRECISION -> CombatFocusModifier(
            accuracyMultiplier = 1.08
        )
        CombatTrainingFocus.RAPID -> CombatFocusModifier(
            attackSpeedMultiplier = 1.08,
            damageMultiplier = 0.96
        )
        CombatTrainingFocus.HEAVY_SHOT -> CombatFocusModifier(
            damageMultiplier = 1.10,
            attackSpeedMultiplier = 0.94
        )
        CombatTrainingFocus.EVASIVE -> CombatFocusModifier(
            dodgeMultiplier = 1.10,
            damageMultiplier = 0.96
        )
        CombatTrainingFocus.DESTRUCTION -> CombatFocusModifier(
            damageMultiplier = 1.08
        )
        CombatTrainingFocus.WARDING -> CombatFocusModifier(
            resistanceMultiplier = 1.10,
            defenseMultiplier = 1.04,
            damageMultiplier = 0.96
        )
        CombatTrainingFocus.RITUAL -> CombatFocusModifier(
            buffDurationMultiplier = 1.12,
            damageMultiplier = 0.96
        )
    }
}
```

### 6.6 Combat Preview

Dungeon setup screen must show:

- Selected combat style.
- Selected training focus.
- XP split.
- Hit chance.
- Max hit.
- Damage reduction.
- Food required estimate.
- Clear chance.
- Expected XP/hour.
- Expected loot/hour.
- Warnings if gear/ammo/runes/food are insufficient.

Example UI text:

```text
Training Focus: Aggressive
XP Split: Attack 10%, Strength 70%, Defense 5%, Hitpoints 15%
Effect: +8% melee damage, -3% accuracy
Estimated Survival: Safe
Food Required: 42
Clear Chance: 78%
```

### 6.7 Combat Result Screen

Show:

- XP gained per combat skill.
- Food consumed.
- Ammo/runes consumed.
- Damage dealt.
- Damage taken.
- Accuracy rate.
- Average hit.
- Loot gained.
- Death/stopped reason if applicable.

---

## 7. Combat Formula Upgrade

### 7.1 Core Stats

Player derived stats:

```kotlin
data class DerivedCombatStats(
    val maxHealth: Int,
    val attackPower: Double,
    val strengthPower: Double,
    val defensePower: Double,
    val rangedPower: Double,
    val magicPower: Double,
    val prayerPower: Double,
    val accuracy: Double,
    val armor: Double,
    val resistance: Double,
    val evasion: Double,
    val critChance: Double,
    val critDamageMultiplier: Double,
    val attackSpeed: Double,
    val damageReduction: Double,
    val foodHealingMultiplier: Double
)
```

### 7.2 Hit Chance

```text
HitChance = clamp(
    0.65
    + ((PlayerAccuracy - EnemyEvasion) / 300.0)
    + StyleAdvantageBonus
    + TalentBonus
    + FocusBonus,
    0.35,
    0.95
)
```

### 7.3 Max Hit

Melee:

```text
MeleeMaxHit =
    WeaponBaseDamage
    * WeaponSpeedModifier
    * (1 + StrengthLevel / 100.0)
    * GearDamageMultiplier
    * TalentMultiplier
    * FocusDamageMultiplier
```

Archery:

```text
RangedMaxHit =
    WeaponBaseDamage
    + AmmoDamage
    * (1 + RangedLevel / 100.0)
    * GearRangedMultiplier
    * TalentMultiplier
    * FocusDamageMultiplier
```

Magic:

```text
MagicMaxHit =
    SpellBaseDamage
    * (1 + MagicLevel / 100.0)
    * StaffMultiplier
    * SchoolMultiplier
    * TalentMultiplier
    * FocusDamageMultiplier
```

### 7.4 Damage Taken

```text
DamageTaken =
    EnemyDamage
    * (100.0 / (100.0 + PlayerDefense + Armor))
    * ResistanceModifier
    * BossMechanicModifier
```

### 7.5 Combat Power

```text
CombatPower =
    WeaponPower
    + ArmorPower
    + AccessoryPower
    + AttackLevel * 2.0
    + StrengthLevel * 2.5
    + DefenseLevel * 2.2
    + HitpointsLevel * 1.8
    + RangedLevel * 2.3
    + MagicLevel * 2.3
    + PrayerLevel * 1.5
    + TalentPower
```

This is a display/helper value, not the only combat calculation.

---

## 8. Damage Types, Weapons, and Enemy Weaknesses

### 8.1 Damage Types

```kotlin
enum class DamageType {
    SLASH,
    CRUSH,
    PIERCE,
    FIRE,
    FROST,
    ARCANE,
    HOLY,
    SHADOW,
    NATURE,
    POISON,
    BLEED,
    LIGHTNING
}
```

### 8.2 Weapon Types

```kotlin
enum class WeaponType {
    SWORD,
    AXE,
    DAGGER,
    MACE,
    SPEAR,
    HAMMER,
    GREAT_SWORD,
    BOW,
    CROSSBOW,
    THROWING_KNIFE,
    STAFF,
    WAND,
    TOME,
    SHIELD,
    RELIC
}
```

### 8.3 Armor Types

```kotlin
enum class ArmorType {
    CLOTH,
    LEATHER,
    MAIL,
    PLATE,
    RELIC
}
```

### 8.4 Enemy Affinity

```kotlin
data class EnemyAffinity(
    val weaknesses: Set<DamageType> = emptySet(),
    val resistances: Set<DamageType> = emptySet(),
    val immunities: Set<DamageType> = emptySet()
)
```

Damage modifier:

```kotlin
fun damageTypeModifier(type: DamageType, affinity: EnemyAffinity): Double {
    return when {
        type in affinity.immunities -> 0.0
        type in affinity.weaknesses -> 1.20
        type in affinity.resistances -> 0.80
        else -> 1.0
    }
}
```

### 8.5 Enemy Examples

| Enemy Type | Weak To | Resistant To |
|---|---|---|
| Undead | Holy, Crush | Poison, Shadow |
| Beast | Pierce, Bleed | Nature |
| Armored | Crush, Arcane | Slash |
| Demon | Holy, Frost | Fire, Shadow |
| Construct | Crush, Lightning | Poison, Bleed |
| Mage | Pierce, Shadow | Arcane |
| Dragon | Frost, Pierce | Fire |

---

## 9. Archery System

### 9.1 Design Goal

Archery should feel like a full combat identity with weapons, ammo, accuracy, crits, and boss matchups.

### 9.2 Ranged Weapon Types

| Weapon | Role |
|---|---|
| Shortbow | Fast attacks, lower damage |
| Longbow | Balanced ranged damage |
| Crossbow | Slower, armor penetration |
| Throwing Knives | Fast, crit-focused |
| Javelin | Heavy ranged hit |

### 9.3 Ammo Slot

Ammo should be an equipment slot.

Rules:

- Bows require arrows.
- Crossbows require bolts.
- Throwing weapons may consume themselves or use a stack.
- Magic does not use ammo slot unless using rune pouch later.
- If ammo runs out, combat stops or falls back to weak unarmed behavior depending on settings.

Ammo metadata:

```kotlin
data class AmmoMetadata(
    val ammoType: AmmoType,
    val baseDamage: Int,
    val damageType: DamageType,
    val compatibleWeapons: Set<WeaponType>
)
```

```kotlin
enum class AmmoType {
    ARROW,
    BOLT,
    THROWING,
    BULLET,
    RUNE_CHARGE
}
```

### 9.4 Ammo Examples

| Ammo | Requirement | Effect |
|---|---|---|
| Bronze Arrow | Ranged 1 | Basic pierce damage |
| Iron Arrow | Ranged 10 | Better pierce damage |
| Fire Arrow | Ranged 25, Firemaking 20 | Adds fire damage |
| Broad Arrow | Slayer 30 | Bonus vs Slayer targets |
| Poison Arrow | Herblore 35 | Poison damage over time |
| Steel Bolt | Ranged 20 | Crossbow damage |
| Piercing Bolt | Ranged 35 | Armor penetration |
| Rune Bolt | Ranged 50, Runecrafting 40 | Magic-ranged hybrid |

---

## 10. Magic System

### 10.1 Design Goal

Magic must not be ranged combat painted blue. It needs spell schools, runes, staves, tomes, and meaningful enemy matchups.

### 10.2 Spell Schools

```kotlin
enum class MagicSchool {
    FIRE,
    FROST,
    ARCANE,
    HOLY,
    SHADOW,
    NATURE,
    BLOOD,
    STORM
}
```

### 10.3 Spell Metadata

```kotlin
data class SpellDefinition(
    val id: String,
    val name: String,
    val school: MagicSchool,
    val requiredMagicLevel: Int,
    val requiredPrayerLevel: Int? = null,
    val requiredRunes: Map<String, Int>,
    val baseDamage: Int,
    val damageType: DamageType,
    val effectIds: List<String> = emptyList()
)
```

### 10.4 Magic Gear

Magic loadout can use:

- Staff or wand in main hand.
- Tome, orb, or shield in off hand.
- Cloth armor.
- Rune pouch later.
- Spell selection in combat setup.

### 10.5 Spell Examples

| Spell | School | Requirement | Effect |
|---|---|---|---|
| Ember Bolt | Fire | Magic 1 | Basic fire damage |
| Frostbind | Frost | Magic 10 | Damage + enemy speed reduction |
| Arcane Lance | Arcane | Magic 20 | High accuracy |
| Rootsnare | Nature | Magic 25 | Damage + poison cleanse bonus |
| Smite Undead | Holy | Magic 30, Prayer 20 | Bonus vs undead |
| Shadow Leech | Shadow | Magic 35 | Damage + small heal |
| Blood Pact | Blood | Magic 45 | Strong damage, self-cost |
| Chain Spark | Storm | Magic 55 | Multi-hit |

### 10.6 Rune Consumption

Rules:

- Each spell consumes rune items.
- Combat preview estimates rune consumption.
- Combat stops if required runes run out.
- Optional setting: switch to cheaper fallback spell when high-tier runes run out.

---

## 11. Race, Class, and Specialization System

### 11.1 Race Enum

```kotlin
enum class CharacterRace {
    HUMAN,
    DWARF,
    ELF,
    ORC,
    UNDEAD,
    GNOME,
    DRAKONID,
    HALFLING
}
```

### 11.2 Race Bonuses

| Race | Bonus | Weakness |
|---|---|---|
| Human | +2% all XP | No specialization bonus |
| Dwarf | +5% Mining, +3% Defense | -2% Magic XP |
| Elf | +5% Ranged/Archery, +3% Magic | -2% Smithing XP |
| Orc | +5% Strength, +3% Hitpoints | -2% Mercantile XP |
| Undead | +5% Necromancy, poison resistance | -3% Prayer healing received |
| Gnome | +5% Engineering, +3% Crafting | -2% Strength XP |
| Drakonid | Fire resistance, +3% melee damage | -2% Fishing/Cooking XP |
| Halfling | +5% Thieving, +3% Evasion | -2% heavy armor effectiveness |

### 11.3 Class Enum

```kotlin
enum class CharacterClass {
    WARRIOR,
    RANGER,
    MAGE,
    CLERIC,
    ROGUE,
    DRUID,
    NECROMANCER,
    ARTIFICER
}
```

### 11.4 Class Roles

| Class | Role | Primary Skills | Preferred Armor | Preferred Weapons |
|---|---|---|---|---|
| Warrior | Melee tank/DPS | Attack, Strength, Defense | Plate | Sword, axe, mace, shield |
| Ranger | Ranged DPS | Ranged/Archery, Evasion, Hunting | Leather | Bow, crossbow |
| Mage | Magic DPS | Magic, Runecrafting, Enchanting | Cloth | Staff, wand |
| Cleric | Holy support | Prayer, Defense, First Aid | Mail/Cloth | Mace, tome, shield |
| Rogue | Crit/evasion | Attack, Evasion, Thieving | Leather | Dagger, shortbow |
| Druid | Nature hybrid | Herbalism, Magic, Prayer | Leather/Cloth | Staff, spear |
| Necromancer | Dark caster | Necromancy, Magic, Prayer | Cloth | Wand, tome |
| Artificer | Gadget/crafting combat | Engineering, Smithing, Enchanting | Mail | Crossbow, gadgets |

### 11.5 Specialization Enum

```kotlin
enum class CharacterSpecialization {
    BERSERKER,
    GUARDIAN,
    WEAPONMASTER,
    MARKSMAN,
    BEASTWARDEN,
    SHADOWSTALKER,
    PYROMANCER,
    FROSTBINDER,
    ARCANIST,
    TEMPLAR,
    ORACLE,
    EXORCIST,
    ASSASSIN,
    DUELIST,
    SABOTEUR,
    WILDHEART,
    GROVEKEEPER,
    STORMCALLER,
    BONECALLER,
    BLOODBINDER,
    PLAGUEWEAVER,
    ENGINEER,
    MECHANIST,
    RUNE_SMITH
}
```

### 11.6 Class to Specialization Map

| Class | Specs |
|---|---|
| Warrior | Berserker, Guardian, Weaponmaster |
| Ranger | Marksman, Beastwarden, Shadowstalker |
| Mage | Pyromancer, Frostbinder, Arcanist |
| Cleric | Templar, Oracle, Exorcist |
| Rogue | Assassin, Duelist, Saboteur |
| Druid | Wildheart, Grovekeeper, Stormcaller |
| Necromancer | Bonecaller, Bloodbinder, Plagueweaver |
| Artificer | Engineer, Mechanist, Rune-Smith |

### 11.7 Character Build Entity

```kotlin
@Entity(tableName = "character_builds")
data class CharacterBuildEntity(
    @PrimaryKey val playerId: String,
    val race: CharacterRace,
    val characterClass: CharacterClass,
    val specialization: CharacterSpecialization,
    val activeTalentLoadoutId: String?,
    val activeEquipmentLoadoutId: String?,
    val createdAt: Long,
    val updatedAt: Long
)
```

### 11.8 Existing Player Migration

Migration defaults:

```text
Race: Human
Class: Warrior
Spec: Weaponmaster
```

Do not force existing players through character creation on first launch after update unless a safe migration screen is built.

Recommended UX:

- Existing player opens app.
- System assigns default Human Warrior Weaponmaster.
- Banner says: "New character identity system added. You have one free race/class/spec selection available."
- Player can change once for free.

---

## 12. Talent Tree System

### 12.1 Talent Tree Types

Each class should eventually have:

- Class tree.
- Specialization tree.
- Utility tree.

MVP:

- One class tree per class.
- One spec tree for the selected spec.

### 12.2 Talent Tree Entity

```kotlin
@Entity(tableName = "talent_trees")
data class TalentTreeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val characterClass: CharacterClass?,
    val specialization: CharacterSpecialization?,
    val description: String
)
```

Static content can live in JSON/data definitions instead of Room if easier.

### 12.3 Talent Node Definition

```kotlin
data class TalentNodeDefinition(
    val id: String,
    val treeId: String,
    val name: String,
    val description: String,
    val maxRank: Int,
    val requiredCharacterLevel: Int,
    val requiredTalentIds: List<String>,
    val row: Int,
    val column: Int,
    val effects: List<TalentEffectDefinition>
)
```

### 12.4 Player Talent Rank Entity

```kotlin
@Entity(
    tableName = "player_talents",
    primaryKeys = ["playerId", "talentId"]
)
data class PlayerTalentRankEntity(
    val playerId: String,
    val talentId: String,
    val rank: Int,
    val updatedAt: Long
)
```

### 12.5 Talent Effects

```kotlin
@Serializable
sealed class TalentEffectDefinition {
    @Serializable
    data class StatMultiplier(
        val stat: StatType,
        val multiplierPerRank: Double
    ) : TalentEffectDefinition()

    @Serializable
    data class SkillXpBonus(
        val skill: SkillType,
        val bonusPercentPerRank: Double
    ) : TalentEffectDefinition()

    @Serializable
    data class UnlockAbility(
        val abilityId: String
    ) : TalentEffectDefinition()

    @Serializable
    data class CombatModifier(
        val modifierType: CombatModifierType,
        val valuePerRank: Double
    ) : TalentEffectDefinition()
}
```

### 12.6 Talent Point Formula

Recommended:

```text
Talent points start at character level 10.
Level 10-50: 1 point every 2 levels.
Level 51-100: 1 point every 3 levels.
Level 101+: prestige or mastery points only.
```

Formula:

```kotlin
fun calculateTalentPoints(characterLevel: Int): Int {
    if (characterLevel < 10) return 0
    val early = ((minOf(characterLevel, 50) - 10) / 2) + 1
    val late = if (characterLevel > 50) (characterLevel - 50) / 3 else 0
    return early + late
}
```

### 12.7 Example Warrior Talents

Class tree:

| Talent | Max Rank | Effect |
|---|---:|---|
| Heavy Training | 5 | +1% Strength XP per rank |
| Weapon Discipline | 5 | +1% melee accuracy per rank |
| Iron Stomach | 3 | +3% food healing in combat per rank |
| Shield Practice | 5 | +1% block/defense effectiveness per rank |
| Battle Rhythm | 3 | +1% attack speed per rank |

Guardian spec tree:

| Talent | Max Rank | Effect |
|---|---:|---|
| Shield Wall | 3 | -3% boss special damage per rank |
| Last Stand | 1 | Survive one lethal hit per dungeon |
| Plate Mastery | 5 | +2% plate armor value per rank |
| Taunting Presence | 3 | Future tank contribution bonus |
| Stand Ground | 3 | +3% offline survival estimate per rank |

Berserker spec tree:

| Talent | Max Rank | Effect |
|---|---:|---|
| Heavy Swing | 5 | +2% axe/greatsword damage per rank |
| Blood Heat | 3 | More damage at low HP |
| Execute | 1 | Bonus damage when enemy below 20% HP |
| Reckless Power | 3 | +Damage, -Defense |
| Rage Loop | 3 | Crits increase attack speed briefly in simulation |

---

## 13. Expanded Skills System

### 13.1 Existing Skills to Preserve

Current documented skills:

Gathering:

- Mining.
- Fishing.
- Woodcutting.
- Farming.
- Firemaking.
- Agility.
- Thieving.

Crafting:

- Smithing.
- Cooking.
- Fletching.
- Crafting.
- Runecrafting.
- Herblore.
- Construction.

Combat:

- Attack.
- Strength.
- Defense.
- Ranged.
- Magic.
- Hitpoints.
- Prayer.

Support:

- Mercantile.
- Slayer.

### 13.2 New Skills Batch 1

Add these first:

| Skill | Category | Purpose |
|---|---|---|
| Evasion | Combat | Dodge, rogue/ranger survival |
| Herbalism | Gathering | Wild herbs, potion plants |
| Hunting | Gathering | Meat, hides, beast trophies |
| Leatherworking | Crafting | Leather armor, quivers |
| Tailoring | Crafting | Cloth armor, robes, bags |
| Enchanting | Crafting | Gear stat upgrades |
| Engineering | Crafting/Support | Bombs, traps, gadgets, future world boss tools |
| Leadership | Support | Guild/future multiplayer contribution |

### 13.3 Future Skills Batch 2

| Skill | Purpose |
|---|---|
| Necromancy | Dark magic combat |
| Summoning | Familiars and combat pets |
| Archaeology | Relics, lore, ancient gear |
| Foraging | Mushrooms, berries, food materials |
| Quarrying | Stone for construction/guild halls |
| Jewelcrafting | Rings, gems, sockets |
| Inscription | Scrolls, glyphs, raid buffs |
| First Aid | Bandages and support healing |
| Cartography | Region and dungeon discovery |
| Beast Mastery | Pets/familiars/hunting synergy |
| Sailing | Island content, sea bosses, trade routes |

### 13.4 Skill Metadata

```kotlin
data class SkillDefinition(
    val id: SkillType,
    val displayName: String,
    val category: SkillCategory,
    val description: String,
    val maxLevel: Int,
    val iconResName: String,
    val unlockLevel: Int = 1,
    val isCombatSkill: Boolean = false
)
```

### 13.5 Skill Action Definition

```kotlin
data class SkillActionDefinition(
    val id: String,
    val skill: SkillType,
    val name: String,
    val requiredLevel: Int,
    val durationSeconds: Int,
    val xpReward: Long,
    val requiredItems: Map<String, Int> = emptyMap(),
    val producedItems: List<LootRollDefinition> = emptyList(),
    val requiredToolSlot: EquipmentSlot? = null,
    val requiredToolTags: Set<String> = emptySet()
)
```

### 13.6 XP Curve

Keep existing XP curve if already working. If replacing or adding explicit formula:

```text
XP_to_next_level = floor(50 * level^2.15 + 100 * level)
```

Post-99/mastery later:

```text
XP_to_next_level = floor(90 * level^2.35 + 250 * level)
```

### 13.7 New Skill Unlock Examples

Evasion:

| Level | Unlock |
|---:|---|
| 1 | Basic dodge training |
| 10 | Light footwork |
| 25 | Rogue stance |
| 40 | Dungeon dodge bonus |
| 60 | Boss special avoidance |
| 80 | Shadowstep passive |
| 99 | Evasion mastery cape |

Herbalism:

| Level | Unlock |
|---:|---|
| 1 | Gather wildleaf |
| 10 | Gather bitterroot |
| 20 | Gather embercap |
| 35 | Gather moonflower |
| 50 | Gather ghostvine |
| 70 | Gather dragonmoss |
| 90 | Gather starbloom |

Hunting:

| Level | Unlock |
|---:|---|
| 1 | Trap rabbits |
| 10 | Hunt boars |
| 20 | Track wolves |
| 35 | Hunt stags |
| 50 | Trap wyverns |
| 70 | Hunt shadowcats |
| 90 | Hunt ancient beasts |

Leatherworking:

| Level | Unlock |
|---:|---|
| 1 | Leather gloves |
| 10 | Leather boots |
| 20 | Hunter vest |
| 35 | Reinforced quiver |
| 50 | Wyvernhide armor |
| 70 | Shadow leather set |
| 90 | Ancient beastmail |

Tailoring:

| Level | Unlock |
|---:|---|
| 1 | Cloth robe |
| 10 | Apprentice hood |
| 25 | Mageweave gloves |
| 40 | Mooncloth robe |
| 60 | Ritual mantle |
| 80 | Starwoven set |
| 99 | Archmage vestments |

Enchanting:

| Level | Unlock |
|---:|---|
| 1 | Minor accuracy enchant |
| 15 | Minor defense enchant |
| 30 | Fire damage enchant |
| 45 | Rune socketing |
| 60 | Epic gear enchant |
| 80 | Legendary augment |
| 99 | Masterwork awakening |

Engineering:

| Level | Unlock |
|---:|---|
| 1 | Copper gearspring |
| 10 | Training bomb |
| 25 | Trap kit |
| 40 | Armor-piercing charge |
| 60 | Boss siege device |
| 80 | Mechanical familiar |
| 99 | Titanbreaker engine |

Leadership:

| Level | Unlock |
|---:|---|
| 1 | Personal rally |
| 15 | Mission efficiency |
| 30 | Guild contribution bonus placeholder |
| 50 | Raid command placeholder |
| 70 | World boss support placeholder |
| 99 | Commander mastery |

---

## 14. Dungeons and Boss Upgrades

### 14.1 Dungeon Setup Must Care About Build

Before starting a dungeon, show:

- Recommended combat style.
- Enemy weaknesses.
- Enemy resistances.
- Required/suggested food.
- Required/suggested armor.
- Required/suggested skill levels.
- Clear chance.
- Expected deaths.
- Expected XP/hour.
- Expected loot/hour.

### 14.2 Dungeon Metadata

```kotlin
data class DungeonDefinition(
    val id: String,
    val name: String,
    val description: String,
    val tier: Int,
    val requiredSkills: List<ItemRequirement>,
    val recommendedPower: Int,
    val enemies: List<String>,
    val bossId: String?,
    val lootTableId: String,
    val weaknessesHint: Set<DamageType>,
    val resistancesHint: Set<DamageType>,
    val unlockRequirements: List<String>
)
```

### 14.3 Boss Mechanics

Idle-friendly mechanics:

| Mechanic | Counter |
|---|---|
| Poison aura | Antidotes, Nature magic, Herbalism/Herblore |
| Armor shell | Crush damage, Engineering bombs |
| Flying phase | Archery bonus |
| Fire breath | Fire resistance food/gear |
| Curse pulse | Prayer, Holy magic, cleansing potion |
| Minion swarm | AoE magic, cleave talent |
| Enrage | DPS check |

### 14.4 Boss Mechanic Data

```kotlin
data class BossMechanicDefinition(
    val id: String,
    val name: String,
    val triggerType: BossMechanicTrigger,
    val triggerValue: Double,
    val effectType: BossMechanicEffectType,
    val counterTags: Set<String>,
    val failurePenalty: BossMechanicPenalty
)
```

Example:

```kotlin
BossMechanicDefinition(
    id = "poison_aura_minor",
    name = "Poison Aura",
    triggerType = BossMechanicTrigger.ALWAYS,
    triggerValue = 1.0,
    effectType = BossMechanicEffectType.DAMAGE_OVER_TIME,
    counterTags = setOf("ANTIDOTE", "NATURE_MAGIC", "POISON_RESISTANCE"),
    failurePenalty = BossMechanicPenalty(extraDamagePercent = 12.0)
)
```

---

## 15. Food, Potions, and Consumables

### 15.1 Food Rules

Food should be persistent inventory consumables.

Rules:

- Player chooses active food stack for combat.
- Auto-eat threshold can be configured.
- Food is consumed during simulated combat.
- Combat stops when food runs out if survival is no longer safe.
- Cooking quality can modify healing.

### 15.2 Auto-Eat Settings

```kotlin
data class AutoEatSettings(
    val enabled: Boolean,
    val healthThresholdPercent: Int,
    val selectedFoodItemId: String?
)
```

### 15.3 Consumable Types

```kotlin
enum class ConsumableType {
    FOOD,
    POTION,
    ELIXIR,
    SCROLL,
    BOMB,
    BANDAGE,
    FEAST
}
```

### 15.4 Potion Examples

| Potion | Skill | Effect |
|---|---|---|
| Minor Strength Potion | Herblore 10 | +Strength for combat session |
| Iron Skin Potion | Herblore 20 | +Defense |
| Eagle Eye Potion | Herblore 30 | +Archery accuracy |
| Arcane Draught | Herblore 35 | +Magic damage |
| Antidote | Herblore 25 | Counters poison |
| Fire Ward Potion | Herblore 40 | Fire resistance |
| Focus Elixir | Herblore 50 | +XP from selected combat focus |

---

## 16. UI Screen Changes

### 16.1 Character Overview Screen

Sections:

- Character name.
- Race.
- Class.
- Specialization.
- Character level.
- Combat power.
- Active equipment loadout.
- Primary combat stats.
- Buttons: Gear, Skills, Talents, Stats, Loadouts.

### 16.2 Equipment Screen

Layout:

```text
[Character Summary]

Head        Neck        Shoulders
Chest       Hands       Cape
Legs        Feet
Main Hand   Off Hand
Ring 1      Ring 2
Trinket 1   Trinket 2
Ammo        Tool

[Stat Changes]
[Selected Item Details]
[Unequip] [Replace]
```

### 16.3 Combat Setup Screen

Layout:

```text
Dungeon: Crypt of Lantern Bones

Combat Style
[Melee] [Archery] [Magic]

Training Focus
[Accurate] [Aggressive] [Defensive] [Controlled]

Loadout
Weapon: Iron Sword
Armor: Steel Platebody
Food: Cooked Trout x120
Ammo/Runes: N/A

Preview
Hit Chance: 78%
Max Hit: 12
Damage Reduction: 18%
Clear Chance: 76%
Expected XP/hour: 14,200
Expected Loot/hour: 3,100 coins

[Start Dungeon]
```

### 16.4 Talent Screen

Layout:

- Class tree tab.
- Spec tree tab.
- Utility tree tab later.
- Talent point counter.
- Node grid.
- Selected node details.
- Spend point button.
- Respec button.

### 16.5 Skill Screen

Update skill grouping:

- Combat.
- Gathering.
- Crafting.
- Support.

For each skill:

- Level.
- XP progress.
- Current action.
- Next unlock.
- Relevant gear/tool.

---

## 17. Room Migration Plan

### 17.1 Migration Phase 1

Add:

- equipment_loadouts table.
- item location fields if needed.
- active equipment loadout pointer.

Default:

- Create one active loadout for existing player.
- If existing game has equipped gear fields, migrate them into loadout slots.
- If existing gear is only session-based, leave loadout empty and do not delete inventory items.

### 17.2 Migration Phase 2

Add combat training focus to active sessions/combat history.

Default:

- Existing melee sessions use CONTROLLED or ATTACK depending current behavior.
- Existing ranged sessions use PRECISION.
- Existing magic sessions use DESTRUCTION.

### 17.3 Migration Phase 3

Add character_builds table.

Default:

- Human.
- Warrior.
- Weaponmaster.
- One-time free class/race/spec change flag.

### 17.4 Migration Phase 4

Add player_talents table.

Default:

- No talents spent.
- Talent points calculated from character level.

### 17.5 Migration Safety Rules

- Never delete player inventory during migration.
- Never unequip items permanently during migration.
- If item mapping fails, return item to inventory.
- Add migration tests for pre-upgrade saves.
- Log migration warnings in debug builds.

---

## 18. Repositories and Use Cases

### 18.1 Equipment Repository

Responsibilities:

- Get active loadout.
- Create default loadout.
- Equip item transaction.
- Unequip item transaction.
- Observe equipment changes.
- Resolve equipped stats.

### 18.2 Combat Repository

Responsibilities:

- Store active combat sessions.
- Store combat style.
- Store training focus.
- Simulate combat ticks.
- Calculate XP splits.
- Calculate loot.
- Calculate food/ammo/rune use.

### 18.3 Character Repository

Responsibilities:

- Character level.
- Race/class/spec.
- Talent points.
- Derived stats.
- Character build changes.

### 18.4 Skill Repository

Responsibilities:

- Skill levels.
- Skill XP.
- Skill actions.
- Offline skill progress.
- Unlock checks.

### 18.5 Talent Repository

Responsibilities:

- Talent definitions.
- Player talent ranks.
- Spend/respec.
- Talent effect aggregation.

---

## 19. Offline Progress Requirements

### 19.1 General Formula

```text
elapsedSeconds = min(now - lastCalculatedAt, offlineCap)
actionsCompleted = floor(elapsedSeconds / actionDuration)
xpGained = actionsCompleted * xpPerAction * offlineModifier
lootRolls = actionsCompleted
```

### 19.2 Combat Offline Formula

```text
expectedKills = elapsedSeconds / averageKillTime
expectedDamageTaken = elapsedSeconds * enemyDamagePerSecond
expectedHealingAvailable = foodCount * foodHealing
survivalSeconds = expectedHealingAvailable / enemyDamagePerSecond
```

If survivalSeconds < elapsedSeconds:

```text
simulate rewards until survivalSeconds
stop session
show stopped reason: food depleted / died / ammo depleted / runes depleted
```

### 19.3 Offline Must Store Selected Build

Combat session must store:

- Combat style.
- Training focus.
- Active equipment loadout ID.
- Selected food.
- Selected spell if magic.
- Selected ammo if archery.
- Auto-eat settings.
- Player derived stats snapshot or recalculation policy.

Recommended:

- Store loadout ID and session config.
- Recalculate on claim using current database, unless gear changed during active session.
- If gear can change while session active, either stop old session or snapshot stats at start.

Simpler MVP:

- Starting a combat session locks the active loadout for that session.
- Changing gear stops current combat session.

---

## 20. Content Implementation Format

Prefer data-driven definitions.

Options:

1. Kotlin object definitions.
2. JSON files parsed with kotlinx.serialization.
3. Hybrid: Kotlin for core enums, JSON for content.

Recommended:

- Keep enums in Kotlin.
- Use JSON or Kotlin data definitions for content.
- Avoid hardcoding formulas inside UI.

### 20.1 Item Definition Example

```json
{
  "id": "iron_longsword",
  "name": "Iron Longsword",
  "rarity": "UNCOMMON",
  "itemType": "EQUIPMENT",
  "equipment": {
    "slot": "MAIN_HAND",
    "weaponType": "SWORD",
    "damageTypes": ["SLASH"],
    "requirements": [
      { "skill": "ATTACK", "level": 10 }
    ]
  },
  "stats": {
    "weaponDamage": 12,
    "accuracy": 8,
    "strengthPower": 4
  }
}
```

### 20.2 Skill Action Example

```json
{
  "id": "gather_wildleaf",
  "skill": "HERBALISM",
  "name": "Gather Wildleaf",
  "requiredLevel": 1,
  "durationSeconds": 8,
  "xpReward": 12,
  "producedItems": [
    { "itemId": "wildleaf", "min": 1, "max": 2, "chance": 0.85 }
  ]
}
```

### 20.3 Talent Example

```json
{
  "id": "warrior_heavy_training",
  "treeId": "warrior_class_tree",
  "name": "Heavy Training",
  "description": "+1% Strength XP per rank.",
  "maxRank": 5,
  "requiredCharacterLevel": 10,
  "requiredTalentIds": [],
  "row": 0,
  "column": 1,
  "effects": [
    {
      "type": "SkillXpBonus",
      "skill": "STRENGTH",
      "bonusPercentPerRank": 1.0
    }
  ]
}
```

---

## 21. Testing Strategy

### 21.1 Unit Tests

Combat:

- XP split correctness.
- Focus modifier correctness.
- Hit chance clamp.
- Damage type weakness/resistance.
- Ammo consumption.
- Rune consumption.
- Food consumption.

Equipment:

- Equip requirements.
- Slot compatibility.
- Two-handed weapon off-hand clearing.
- Persistent loadout.
- Stat recalculation.

Talents:

- Talent point calculation.
- Requirement validation.
- Spend point.
- Respec.
- Effect aggregation.

Skills:

- New skill XP gain.
- Unlock requirements.
- Offline action calculation.
- Produced item rolls.

### 21.2 Integration Tests

- New player creates race/class/spec.
- Existing player migration creates default build.
- Equip gear and start dungeon.
- Start melee combat with Aggressive focus, claim results, Strength gets correct XP.
- Start magic combat, runes consumed.
- Start archery combat, arrows consumed.
- Offline progress claim respects selected combat focus.
- Talents modify derived stats.

### 21.3 Manual QA Checklist

- App installs clean.
- Existing saves load.
- No inventory loss after migration.
- Gear stays equipped after restart.
- Gear stays equipped after dungeon.
- Gear stays equipped after offline claim.
- Dungeon preview updates when gear changes.
- Training focus updates XP preview.
- Combat result shows per-skill XP.
- Talent screen handles locked/unlocked nodes.
- New skills display in correct groups.

---

## 22. Build Order Checklist

### Phase 1 Checklist: Persistent Equipment

- [ ] Add EquipmentSlot enum.
- [ ] Add ItemLocation enum if missing.
- [ ] Add EquipmentLoadoutEntity.
- [ ] Add DAO methods.
- [ ] Add Room migration.
- [ ] Add EquipmentRepository.
- [ ] Add EquipItemUseCase.
- [ ] Add UnequipItemUseCase.
- [ ] Add StatCalculator integration.
- [ ] Add Equipment screen.
- [ ] Add item detail equip button.
- [ ] Add tests.

### Phase 2 Checklist: Combat Training Focus

- [ ] Add CombatTrainingFocus enum.
- [ ] Add valid focus map by combat style.
- [ ] Add XP split functions.
- [ ] Add focus modifiers.
- [ ] Add focus to combat session config.
- [ ] Add Room migration.
- [ ] Add combat setup selector.
- [ ] Add combat preview XP split.
- [ ] Add result screen XP breakdown.
- [ ] Add tests.

### Phase 3 Checklist: Archery and Magic

- [ ] Add DamageType enum.
- [ ] Add WeaponType enum.
- [ ] Add AmmoType enum.
- [ ] Add ammo slot support.
- [ ] Add ammo compatibility validation.
- [ ] Add MagicSchool enum.
- [ ] Add SpellDefinition.
- [ ] Add rune consumption.
- [ ] Add enemy affinity metadata.
- [ ] Add style effectiveness preview.
- [ ] Add tests.

### Phase 4 Checklist: Race/Class/Spec

- [ ] Add CharacterRace enum.
- [ ] Add CharacterClass enum.
- [ ] Add CharacterSpecialization enum.
- [ ] Add CharacterBuildEntity.
- [ ] Add Room migration.
- [ ] Add race/class/spec metadata.
- [ ] Add character creation flow.
- [ ] Add existing player default migration.
- [ ] Add one-time free respec/change flag.
- [ ] Add tests.

### Phase 5 Checklist: Talent Trees

- [ ] Add TalentTree definition.
- [ ] Add TalentNode definition.
- [ ] Add TalentEffect definition.
- [ ] Add PlayerTalentRankEntity.
- [ ] Add TalentRepository.
- [ ] Add SpendTalentPointUseCase.
- [ ] Add RespecTalentsUseCase.
- [ ] Add Talent screen.
- [ ] Add talent effect aggregation into StatCalculator.
- [ ] Add tests.

### Phase 6 Checklist: Expanded Skills

- [ ] Add Evasion.
- [ ] Add Herbalism.
- [ ] Add Hunting.
- [ ] Add Leatherworking.
- [ ] Add Tailoring.
- [ ] Add Enchanting.
- [ ] Add Engineering.
- [ ] Add Leadership.
- [ ] Add actions/recipes/items for each.
- [ ] Add offline support.
- [ ] Add tests.

---

## 23. 30-Day Execution Plan

### Week 1: Equipment and Stat Foundation

Day 1:

- Audit current item/equipment code.
- Identify current equipped item handling.
- Add implementation notes to this doc if repo-specific class names differ.

Day 2:

- Add EquipmentSlot enum.
- Add EquipmentLoadoutEntity.
- Add DAO and migration.

Day 3:

- Add EquipItemUseCase.
- Add UnequipItemUseCase.
- Add requirement validation.

Day 4:

- Add stat recalculation pipeline.
- Wire equipped gear into combat stats.

Day 5:

- Build Equipment screen.
- Add equip/replace/unequip UI.

Day 6:

- Add persistence tests.
- Test app restart.
- Fix migration issues.

Day 7:

- Buffer day.
- Manual QA.
- Merge if stable.

### Week 2: Combat Training Focus

Day 8:

- Add CombatTrainingFocus enum.
- Add XP split functions.

Day 9:

- Add focus modifiers.
- Wire into combat simulation.

Day 10:

- Add combat setup selector.
- Add XP split preview.

Day 11:

- Add combat result XP breakdown.
- Add stopped reasons.

Day 12:

- Add offline progress support.
- Confirm focus persists.

Day 13:

- Add tests.
- Tune XP split numbers.

Day 14:

- Manual QA.
- Merge if stable.

### Week 3: Archery and Magic Upgrade

Day 15:

- Add DamageType, WeaponType, ArmorType.
- Add enemy affinity metadata.

Day 16:

- Add ammo slot and ammo metadata.
- Add compatibility validation.

Day 17:

- Add archery/ranged focus options.
- Add ammo consumption in combat.

Day 18:

- Add MagicSchool and SpellDefinition.
- Add spell selection.

Day 19:

- Add rune consumption.
- Add magic focus options.

Day 20:

- Add combat preview style effectiveness.
- Add tests.

Day 21:

- Manual QA.
- Merge if stable.

### Week 4: Character Identity and Talent MVP

Day 22:

- Add CharacterRace, CharacterClass, CharacterSpecialization.
- Add CharacterBuildEntity.

Day 23:

- Add migration defaults.
- Add race/class/spec metadata.

Day 24:

- Add character creation screen.
- Add character overview screen.

Day 25:

- Add TalentTree and TalentNode models.
- Add PlayerTalentRankEntity.

Day 26:

- Add talent point calculation.
- Add spend/respec use cases.

Day 27:

- Add Warrior class tree MVP.
- Add Ranger/Mage placeholder trees.

Day 28:

- Add Talent screen.
- Wire talents into StatCalculator.

Day 29:

- Add tests.
- Manual QA.

Day 30:

- Polish.
- Update README.
- Prepare next roadmap for expanded skills.

---

## 24. Multiplayer-Ready Hooks for Later

Do not build full multiplayer first. Add hooks now so the future backend is not a complete rewrite.

Track these fields in combat history:

- Damage dealt.
- Damage taken.
- Healing consumed.
- Ammo consumed.
- Runes consumed.
- Boss mechanics countered.
- Deaths.
- Clear time.
- Support effects.
- Utility items used.

These become useful later for:

- World boss contribution.
- Guild raid contribution.
- Leaderboards.
- Anti-cheat validation.

Future server-authoritative systems:

- Accounts.
- Cloud saves.
- Real multiplayer guilds.
- World boss events.
- Leaderboards.
- Seasonal resets.
- Anti-cheat scoring.

Keep offline-first local gameplay intact.

---

## 25. Definition of Done

This blueprint is considered implemented when:

- Equipped gear is persistent and slot-based.
- Combat style and training focus are separate.
- Melee can train Attack, Strength, Defense, or Controlled.
- Archery/Ranged has ammo and focus options.
- Magic has spell schools and rune costs.
- Race/class/spec identity exists and persists.
- Talent tree MVP works.
- At least 8 new skills are added or scaffolded.
- Dungeon preview reflects gear, style, focus, food, ammo, runes, and enemy affinity.
- Combat result clearly shows XP by skill and resource consumption.
- Offline combat respects gear, style, focus, food, ammo, and runes.
- Existing saves migrate safely.
- Tests cover the new systems.

---

## 26. Final Implementation Priority

Build in this order:

1. Persistent equipment.
2. Combat training focus.
3. Combat formula/stat cleanup.
4. Archery ammo and weapon identity.
5. Magic spells, schools, and rune costs.
6. Race/class/spec identity.
7. Talent tree MVP.
8. New skills batch 1.
9. Better dungeon boss mechanics.
10. Multiplayer-ready contribution tracking.

Do not start with raids, real guilds, server shards, PvP, marketplace, or world bosses until the character/combat foundation is solid.

The foundation must become fun before the game becomes large. Otherwise it becomes large and dull, which is basically enterprise software with goblins.
