/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.codeforces.commons.reflection.Name;
import java.util.Arrays;

public class m {
    private final long randomSeed;
    private final int tickCount;
    private final double mapSize;
    private final boolean skillsEnabled;
    private final boolean rawMessagesEnabled;
    private final double friendlyFireDamageFactor;
    private final double buildingDamageScoreFactor;
    private final double buildingEliminationScoreFactor;
    private final double minionDamageScoreFactor;
    private final double minionEliminationScoreFactor;
    private final double wizardDamageScoreFactor;
    private final double wizardEliminationScoreFactor;
    private final double teamWorkingScoreFactor;
    private final int victoryScore;
    private final double scoreGainRange;
    private final int rawMessageMaxLength;
    private final double rawMessageTransmissionSpeed;
    private final double wizardRadius;
    private final double wizardCastRange;
    private final double wizardVisionRange;
    private final double wizardForwardSpeed;
    private final double wizardBackwardSpeed;
    private final double wizardStrafeSpeed;
    private final int wizardBaseLife;
    private final int wizardLifeGrowthPerLevel;
    private final int wizardBaseMana;
    private final int wizardManaGrowthPerLevel;
    private final double wizardBaseLifeRegeneration;
    private final double wizardLifeRegenerationGrowthPerLevel;
    private final double wizardBaseManaRegeneration;
    private final double wizardManaRegenerationGrowthPerLevel;
    private final double wizardMaxTurnAngle;
    private final int wizardMaxResurrectionDelayTicks;
    private final int wizardMinResurrectionDelayTicks;
    private final int wizardActionCooldownTicks;
    private final int staffCooldownTicks;
    private final int magicMissileCooldownTicks;
    private final int frostBoltCooldownTicks;
    private final int fireballCooldownTicks;
    private final int hasteCooldownTicks;
    private final int shieldCooldownTicks;
    private final int magicMissileManacost;
    private final int frostBoltManacost;
    private final int fireballManacost;
    private final int hasteManacost;
    private final int shieldManacost;
    private final int staffDamage;
    private final double staffSector;
    private final double staffRange;
    private final int[] levelUpXpValues;
    private final double minionRadius;
    private final double minionVisionRange;
    private final double minionSpeed;
    private final double minionMaxTurnAngle;
    private final int minionLife;
    private final int factionMinionAppearanceIntervalTicks;
    private final int orcWoodcutterActionCooldownTicks;
    private final int orcWoodcutterDamage;
    private final double orcWoodcutterAttackSector;
    private final double orcWoodcutterAttackRange;
    private final int fetishBlowdartActionCooldownTicks;
    private final double fetishBlowdartAttackRange;
    private final double fetishBlowdartAttackSector;
    private final double bonusRadius;
    private final int bonusAppearanceIntervalTicks;
    private final int bonusScoreAmount;
    private final double dartRadius;
    private final double dartSpeed;
    private final int dartDirectDamage;
    private final double magicMissileRadius;
    private final double magicMissileSpeed;
    private final int magicMissileDirectDamage;
    private final double frostBoltRadius;
    private final double frostBoltSpeed;
    private final int frostBoltDirectDamage;
    private final double fireballRadius;
    private final double fireballSpeed;
    private final double fireballExplosionMaxDamageRange;
    private final double fireballExplosionMinDamageRange;
    private final int fireballExplosionMaxDamage;
    private final int fireballExplosionMinDamage;
    private final double guardianTowerRadius;
    private final double guardianTowerVisionRange;
    private final double guardianTowerLife;
    private final double guardianTowerAttackRange;
    private final int guardianTowerDamage;
    private final int guardianTowerCooldownTicks;
    private final double factionBaseRadius;
    private final double factionBaseVisionRange;
    private final double factionBaseLife;
    private final double factionBaseAttackRange;
    private final int factionBaseDamage;
    private final int factionBaseCooldownTicks;
    private final int burningDurationTicks;
    private final int burningSummaryDamage;
    private final int empoweredDurationTicks;
    private final double empoweredDamageFactor;
    private final int frozenDurationTicks;
    private final int hastenedDurationTicks;
    private final double hastenedBonusDurationFactor;
    private final double hastenedMovementBonusFactor;
    private final double hastenedRotationBonusFactor;
    private final int shieldedDurationTicks;
    private final double shieldedBonusDurationFactor;
    private final double shieldedDirectDamageAbsorptionFactor;
    private final double auraSkillRange;
    private final double rangeBonusPerSkillLevel;
    private final int magicalDamageBonusPerSkillLevel;
    private final int staffDamageBonusPerSkillLevel;
    private final double movementBonusFactorPerSkillLevel;
    private final int magicalDamageAbsorptionPerSkillLevel;

    public m(@Name(value="randomSeed") long l2, @Name(value="tickCount") int n2, @Name(value="mapSize") double d2, @Name(value="skillsEnabled") boolean bl, @Name(value="rawMessagesEnabled") boolean bl2, @Name(value="friendlyFireDamageFactor") double d3, @Name(value="buildingDamageScoreFactor") double d4, @Name(value="buildingEliminationScoreFactor") double d5, @Name(value="minionDamageScoreFactor") double d6, @Name(value="minionEliminationScoreFactor") double d7, @Name(value="wizardDamageScoreFactor") double d8, @Name(value="wizardEliminationScoreFactor") double d9, @Name(value="teamWorkingScoreFactor") double d10, @Name(value="victoryScore") int n3, @Name(value="scoreGainRange") double d11, @Name(value="rawMessageMaxLength") int n4, @Name(value="rawMessageTransmissionSpeed") double d12, @Name(value="wizardRadius") double d13, @Name(value="wizardCastRange") double d14, @Name(value="wizardVisionRange") double d15, @Name(value="wizardForwardSpeed") double d16, @Name(value="wizardBackwardSpeed") double d17, @Name(value="wizardStrafeSpeed") double d18, @Name(value="wizardBaseLife") int n5, @Name(value="wizardLifeGrowthPerLevel") int n6, @Name(value="wizardBaseMana") int n7, @Name(value="wizardManaGrowthPerLevel") int n8, @Name(value="wizardBaseLifeRegeneration") double d19, @Name(value="wizardLifeRegenerationGrowthPerLevel") double d20, @Name(value="wizardBaseManaRegeneration") double d21, @Name(value="wizardManaRegenerationGrowthPerLevel") double d22, @Name(value="wizardMaxTurnAngle") double d23, @Name(value="wizardMaxResurrectionDelayTicks") int n9, @Name(value="wizardMinResurrectionDelayTicks") int n10, @Name(value="wizardActionCooldownTicks") int n11, @Name(value="staffCooldownTicks") int n12, @Name(value="magicMissileCooldownTicks") int n13, @Name(value="frostBoltCooldownTicks") int n14, @Name(value="fireballCooldownTicks") int n15, @Name(value="hasteCooldownTicks") int n16, @Name(value="shieldCooldownTicks") int n17, @Name(value="magicMissileManacost") int n18, @Name(value="frostBoltManacost") int n19, @Name(value="fireballManacost") int n20, @Name(value="hasteManacost") int n21, @Name(value="shieldManacost") int n22, @Name(value="staffDamage") int n23, @Name(value="staffSector") double d24, @Name(value="staffRange") double d25, @Name(value="levelUpXpValues") int[] arrn, @Name(value="minionRadius") double d26, @Name(value="minionVisionRange") double d27, @Name(value="minionSpeed") double d28, @Name(value="minionMaxTurnAngle") double d29, @Name(value="minionLife") int n24, @Name(value="factionMinionAppearanceIntervalTicks") int n25, @Name(value="orcWoodcutterActionCooldownTicks") int n26, @Name(value="orcWoodcutterDamage") int n27, @Name(value="orcWoodcutterAttackSector") double d30, @Name(value="orcWoodcutterAttackRange") double d31, @Name(value="fetishBlowdartActionCooldownTicks") int n28, @Name(value="fetishBlowdartAttackRange") double d32, @Name(value="fetishBlowdartAttackSector") double d33, @Name(value="bonusRadius") double d34, @Name(value="bonusAppearanceIntervalTicks") int n29, @Name(value="bonusScoreAmount") int n30, @Name(value="dartRadius") double d35, @Name(value="dartSpeed") double d36, @Name(value="dartDirectDamage") int n31, @Name(value="magicMissileRadius") double d37, @Name(value="magicMissileSpeed") double d38, @Name(value="magicMissileDirectDamage") int n32, @Name(value="frostBoltRadius") double d39, @Name(value="frostBoltSpeed") double d40, @Name(value="frostBoltDirectDamage") int n33, @Name(value="fireballRadius") double d41, @Name(value="fireballSpeed") double d42, @Name(value="fireballExplosionMaxDamageRange") double d43, @Name(value="fireballExplosionMinDamageRange") double d44, @Name(value="fireballExplosionMaxDamage") int n34, @Name(value="fireballExplosionMinDamage") int n35, @Name(value="guardianTowerRadius") double d45, @Name(value="guardianTowerVisionRange") double d46, @Name(value="guardianTowerLife") double d47, @Name(value="guardianTowerAttackRange") double d48, @Name(value="guardianTowerDamage") int n36, @Name(value="guardianTowerCooldownTicks") int n37, @Name(value="factionBaseRadius") double d49, @Name(value="factionBaseVisionRange") double d50, @Name(value="factionBaseLife") double d51, @Name(value="factionBaseAttackRange") double d52, @Name(value="factionBaseDamage") int n38, @Name(value="factionBaseCooldownTicks") int n39, @Name(value="burningDurationTicks") int n40, @Name(value="burningSummaryDamage") int n41, @Name(value="empoweredDurationTicks") int n42, @Name(value="empoweredDamageFactor") double d53, @Name(value="frozenDurationTicks") int n43, @Name(value="hastenedDurationTicks") int n44, @Name(value="hastenedBonusDurationFactor") double d54, @Name(value="hastenedMovementBonusFactor") double d55, @Name(value="hastenedRotationBonusFactor") double d56, @Name(value="shieldedDurationTicks") int n45, @Name(value="shieldedBonusDurationFactor") double d57, @Name(value="shieldedDirectDamageAbsorptionFactor") double d58, @Name(value="auraSkillRange") double d59, @Name(value="rangeBonusPerSkillLevel") double d60, @Name(value="magicalDamageBonusPerSkillLevel") int n46, @Name(value="staffDamageBonusPerSkillLevel") int n47, @Name(value="movementBonusFactorPerSkillLevel") double d61, @Name(value="magicalDamageAbsorptionPerSkillLevel") int n48) {
        this.randomSeed = l2;
        this.tickCount = n2;
        this.mapSize = d2;
        this.skillsEnabled = bl;
        this.rawMessagesEnabled = bl2;
        this.friendlyFireDamageFactor = d3;
        this.buildingDamageScoreFactor = d4;
        this.buildingEliminationScoreFactor = d5;
        this.minionDamageScoreFactor = d6;
        this.minionEliminationScoreFactor = d7;
        this.wizardDamageScoreFactor = d8;
        this.wizardEliminationScoreFactor = d9;
        this.teamWorkingScoreFactor = d10;
        this.victoryScore = n3;
        this.scoreGainRange = d11;
        this.rawMessageMaxLength = n4;
        this.rawMessageTransmissionSpeed = d12;
        this.wizardRadius = d13;
        this.wizardCastRange = d14;
        this.wizardVisionRange = d15;
        this.wizardForwardSpeed = d16;
        this.wizardBackwardSpeed = d17;
        this.wizardStrafeSpeed = d18;
        this.wizardBaseLife = n5;
        this.wizardLifeGrowthPerLevel = n6;
        this.wizardBaseMana = n7;
        this.wizardManaGrowthPerLevel = n8;
        this.wizardBaseLifeRegeneration = d19;
        this.wizardLifeRegenerationGrowthPerLevel = d20;
        this.wizardBaseManaRegeneration = d21;
        this.wizardManaRegenerationGrowthPerLevel = d22;
        this.wizardMaxTurnAngle = d23;
        this.wizardMaxResurrectionDelayTicks = n9;
        this.wizardMinResurrectionDelayTicks = n10;
        this.wizardActionCooldownTicks = n11;
        this.staffCooldownTicks = n12;
        this.magicMissileCooldownTicks = n13;
        this.frostBoltCooldownTicks = n14;
        this.fireballCooldownTicks = n15;
        this.hasteCooldownTicks = n16;
        this.shieldCooldownTicks = n17;
        this.magicMissileManacost = n18;
        this.frostBoltManacost = n19;
        this.fireballManacost = n20;
        this.hasteManacost = n21;
        this.shieldManacost = n22;
        this.staffDamage = n23;
        this.staffSector = d24;
        this.staffRange = d25;
        this.levelUpXpValues = Arrays.copyOf(arrn, arrn.length);
        this.minionRadius = d26;
        this.minionVisionRange = d27;
        this.minionSpeed = d28;
        this.minionMaxTurnAngle = d29;
        this.minionLife = n24;
        this.factionMinionAppearanceIntervalTicks = n25;
        this.orcWoodcutterActionCooldownTicks = n26;
        this.orcWoodcutterDamage = n27;
        this.orcWoodcutterAttackSector = d30;
        this.orcWoodcutterAttackRange = d31;
        this.fetishBlowdartActionCooldownTicks = n28;
        this.fetishBlowdartAttackRange = d32;
        this.fetishBlowdartAttackSector = d33;
        this.bonusRadius = d34;
        this.bonusAppearanceIntervalTicks = n29;
        this.bonusScoreAmount = n30;
        this.dartRadius = d35;
        this.dartSpeed = d36;
        this.dartDirectDamage = n31;
        this.magicMissileRadius = d37;
        this.magicMissileSpeed = d38;
        this.magicMissileDirectDamage = n32;
        this.frostBoltRadius = d39;
        this.frostBoltSpeed = d40;
        this.frostBoltDirectDamage = n33;
        this.fireballRadius = d41;
        this.fireballSpeed = d42;
        this.fireballExplosionMaxDamageRange = d43;
        this.fireballExplosionMinDamageRange = d44;
        this.fireballExplosionMaxDamage = n34;
        this.fireballExplosionMinDamage = n35;
        this.guardianTowerRadius = d45;
        this.guardianTowerVisionRange = d46;
        this.guardianTowerLife = d47;
        this.guardianTowerAttackRange = d48;
        this.guardianTowerDamage = n36;
        this.guardianTowerCooldownTicks = n37;
        this.factionBaseRadius = d49;
        this.factionBaseVisionRange = d50;
        this.factionBaseLife = d51;
        this.factionBaseAttackRange = d52;
        this.factionBaseDamage = n38;
        this.factionBaseCooldownTicks = n39;
        this.burningDurationTicks = n40;
        this.burningSummaryDamage = n41;
        this.empoweredDurationTicks = n42;
        this.empoweredDamageFactor = d53;
        this.frozenDurationTicks = n43;
        this.hastenedDurationTicks = n44;
        this.hastenedBonusDurationFactor = d54;
        this.hastenedMovementBonusFactor = d55;
        this.hastenedRotationBonusFactor = d56;
        this.shieldedDurationTicks = n45;
        this.shieldedBonusDurationFactor = d57;
        this.shieldedDirectDamageAbsorptionFactor = d58;
        this.auraSkillRange = d59;
        this.rangeBonusPerSkillLevel = d60;
        this.magicalDamageBonusPerSkillLevel = n46;
        this.staffDamageBonusPerSkillLevel = n47;
        this.movementBonusFactorPerSkillLevel = d61;
        this.magicalDamageAbsorptionPerSkillLevel = n48;
    }

    public long getRandomSeed() {
        return this.randomSeed;
    }

    public int getTickCount() {
        return this.tickCount;
    }

    public double getMapSize() {
        return this.mapSize;
    }

    public boolean isSkillsEnabled() {
        return this.skillsEnabled;
    }

    public boolean isRawMessagesEnabled() {
        return this.rawMessagesEnabled;
    }

    public double getFriendlyFireDamageFactor() {
        return this.friendlyFireDamageFactor;
    }

    public double getBuildingDamageScoreFactor() {
        return this.buildingDamageScoreFactor;
    }

    public double getBuildingEliminationScoreFactor() {
        return this.buildingEliminationScoreFactor;
    }

    public double getMinionDamageScoreFactor() {
        return this.minionDamageScoreFactor;
    }

    public double getMinionEliminationScoreFactor() {
        return this.minionEliminationScoreFactor;
    }

    public double getWizardDamageScoreFactor() {
        return this.wizardDamageScoreFactor;
    }

    public double getWizardEliminationScoreFactor() {
        return this.wizardEliminationScoreFactor;
    }

    public double getTeamWorkingScoreFactor() {
        return this.teamWorkingScoreFactor;
    }

    public int getVictoryScore() {
        return this.victoryScore;
    }

    public double getScoreGainRange() {
        return this.scoreGainRange;
    }

    public int getRawMessageMaxLength() {
        return this.rawMessageMaxLength;
    }

    public double getRawMessageTransmissionSpeed() {
        return this.rawMessageTransmissionSpeed;
    }

    public double getWizardRadius() {
        return this.wizardRadius;
    }

    public double getWizardCastRange() {
        return this.wizardCastRange;
    }

    public double getWizardVisionRange() {
        return this.wizardVisionRange;
    }

    public double getWizardForwardSpeed() {
        return this.wizardForwardSpeed;
    }

    public double getWizardBackwardSpeed() {
        return this.wizardBackwardSpeed;
    }

    public double getWizardStrafeSpeed() {
        return this.wizardStrafeSpeed;
    }

    public int getWizardBaseLife() {
        return this.wizardBaseLife;
    }

    public int getWizardLifeGrowthPerLevel() {
        return this.wizardLifeGrowthPerLevel;
    }

    public int getWizardBaseMana() {
        return this.wizardBaseMana;
    }

    public int getWizardManaGrowthPerLevel() {
        return this.wizardManaGrowthPerLevel;
    }

    public double getWizardBaseLifeRegeneration() {
        return this.wizardBaseLifeRegeneration;
    }

    public double getWizardLifeRegenerationGrowthPerLevel() {
        return this.wizardLifeRegenerationGrowthPerLevel;
    }

    public double getWizardBaseManaRegeneration() {
        return this.wizardBaseManaRegeneration;
    }

    public double getWizardManaRegenerationGrowthPerLevel() {
        return this.wizardManaRegenerationGrowthPerLevel;
    }

    public double getWizardMaxTurnAngle() {
        return this.wizardMaxTurnAngle;
    }

    public int getWizardMaxResurrectionDelayTicks() {
        return this.wizardMaxResurrectionDelayTicks;
    }

    public int getWizardMinResurrectionDelayTicks() {
        return this.wizardMinResurrectionDelayTicks;
    }

    public int getWizardActionCooldownTicks() {
        return this.wizardActionCooldownTicks;
    }

    public int getStaffCooldownTicks() {
        return this.staffCooldownTicks;
    }

    public int getMagicMissileCooldownTicks() {
        return this.magicMissileCooldownTicks;
    }

    public int getFrostBoltCooldownTicks() {
        return this.frostBoltCooldownTicks;
    }

    public int getFireballCooldownTicks() {
        return this.fireballCooldownTicks;
    }

    public int getHasteCooldownTicks() {
        return this.hasteCooldownTicks;
    }

    public int getShieldCooldownTicks() {
        return this.shieldCooldownTicks;
    }

    public int getMagicMissileManacost() {
        return this.magicMissileManacost;
    }

    public int getFrostBoltManacost() {
        return this.frostBoltManacost;
    }

    public int getFireballManacost() {
        return this.fireballManacost;
    }

    public int getHasteManacost() {
        return this.hasteManacost;
    }

    public int getShieldManacost() {
        return this.shieldManacost;
    }

    public int getStaffDamage() {
        return this.staffDamage;
    }

    public double getStaffSector() {
        return this.staffSector;
    }

    public double getStaffRange() {
        return this.staffRange;
    }

    public int[] getLevelUpXpValues() {
        return this.levelUpXpValues.length == 0 ? this.levelUpXpValues : Arrays.copyOf(this.levelUpXpValues, this.levelUpXpValues.length);
    }

    public double getMinionRadius() {
        return this.minionRadius;
    }

    public double getMinionVisionRange() {
        return this.minionVisionRange;
    }

    public double getMinionSpeed() {
        return this.minionSpeed;
    }

    public double getMinionMaxTurnAngle() {
        return this.minionMaxTurnAngle;
    }

    public int getMinionLife() {
        return this.minionLife;
    }

    public int getFactionMinionAppearanceIntervalTicks() {
        return this.factionMinionAppearanceIntervalTicks;
    }

    public int getOrcWoodcutterActionCooldownTicks() {
        return this.orcWoodcutterActionCooldownTicks;
    }

    public int getOrcWoodcutterDamage() {
        return this.orcWoodcutterDamage;
    }

    public double getOrcWoodcutterAttackSector() {
        return this.orcWoodcutterAttackSector;
    }

    public double getOrcWoodcutterAttackRange() {
        return this.orcWoodcutterAttackRange;
    }

    public int getFetishBlowdartActionCooldownTicks() {
        return this.fetishBlowdartActionCooldownTicks;
    }

    public double getFetishBlowdartAttackRange() {
        return this.fetishBlowdartAttackRange;
    }

    public double getFetishBlowdartAttackSector() {
        return this.fetishBlowdartAttackSector;
    }

    public double getBonusRadius() {
        return this.bonusRadius;
    }

    public int getBonusAppearanceIntervalTicks() {
        return this.bonusAppearanceIntervalTicks;
    }

    public int getBonusScoreAmount() {
        return this.bonusScoreAmount;
    }

    public double getDartRadius() {
        return this.dartRadius;
    }

    public double getDartSpeed() {
        return this.dartSpeed;
    }

    public int getDartDirectDamage() {
        return this.dartDirectDamage;
    }

    public double getMagicMissileRadius() {
        return this.magicMissileRadius;
    }

    public double getMagicMissileSpeed() {
        return this.magicMissileSpeed;
    }

    public int getMagicMissileDirectDamage() {
        return this.magicMissileDirectDamage;
    }

    public double getFrostBoltRadius() {
        return this.frostBoltRadius;
    }

    public double getFrostBoltSpeed() {
        return this.frostBoltSpeed;
    }

    public int getFrostBoltDirectDamage() {
        return this.frostBoltDirectDamage;
    }

    public double getFireballRadius() {
        return this.fireballRadius;
    }

    public double getFireballSpeed() {
        return this.fireballSpeed;
    }

    public double getFireballExplosionMaxDamageRange() {
        return this.fireballExplosionMaxDamageRange;
    }

    public double getFireballExplosionMinDamageRange() {
        return this.fireballExplosionMinDamageRange;
    }

    public int getFireballExplosionMaxDamage() {
        return this.fireballExplosionMaxDamage;
    }

    public int getFireballExplosionMinDamage() {
        return this.fireballExplosionMinDamage;
    }

    public double getGuardianTowerRadius() {
        return this.guardianTowerRadius;
    }

    public double getGuardianTowerVisionRange() {
        return this.guardianTowerVisionRange;
    }

    public double getGuardianTowerLife() {
        return this.guardianTowerLife;
    }

    public double getGuardianTowerAttackRange() {
        return this.guardianTowerAttackRange;
    }

    public int getGuardianTowerDamage() {
        return this.guardianTowerDamage;
    }

    public int getGuardianTowerCooldownTicks() {
        return this.guardianTowerCooldownTicks;
    }

    public double getFactionBaseRadius() {
        return this.factionBaseRadius;
    }

    public double getFactionBaseVisionRange() {
        return this.factionBaseVisionRange;
    }

    public double getFactionBaseLife() {
        return this.factionBaseLife;
    }

    public double getFactionBaseAttackRange() {
        return this.factionBaseAttackRange;
    }

    public int getFactionBaseDamage() {
        return this.factionBaseDamage;
    }

    public int getFactionBaseCooldownTicks() {
        return this.factionBaseCooldownTicks;
    }

    public int getBurningDurationTicks() {
        return this.burningDurationTicks;
    }

    public int getBurningSummaryDamage() {
        return this.burningSummaryDamage;
    }

    public int getEmpoweredDurationTicks() {
        return this.empoweredDurationTicks;
    }

    public double getEmpoweredDamageFactor() {
        return this.empoweredDamageFactor;
    }

    public int getFrozenDurationTicks() {
        return this.frozenDurationTicks;
    }

    public int getHastenedDurationTicks() {
        return this.hastenedDurationTicks;
    }

    public double getHastenedBonusDurationFactor() {
        return this.hastenedBonusDurationFactor;
    }

    public double getHastenedMovementBonusFactor() {
        return this.hastenedMovementBonusFactor;
    }

    public double getHastenedRotationBonusFactor() {
        return this.hastenedRotationBonusFactor;
    }

    public int getShieldedDurationTicks() {
        return this.shieldedDurationTicks;
    }

    public double getShieldedBonusDurationFactor() {
        return this.shieldedBonusDurationFactor;
    }

    public double getShieldedDirectDamageAbsorptionFactor() {
        return this.shieldedDirectDamageAbsorptionFactor;
    }

    public double getAuraSkillRange() {
        return this.auraSkillRange;
    }

    public double getRangeBonusPerSkillLevel() {
        return this.rangeBonusPerSkillLevel;
    }

    public int getMagicalDamageBonusPerSkillLevel() {
        return this.magicalDamageBonusPerSkillLevel;
    }

    public int getStaffDamageBonusPerSkillLevel() {
        return this.staffDamageBonusPerSkillLevel;
    }

    public double getMovementBonusFactorPerSkillLevel() {
        return this.movementBonusFactorPerSkillLevel;
    }

    public int getMagicalDamageAbsorptionPerSkillLevel() {
        return this.magicalDamageAbsorptionPerSkillLevel;
    }
}

