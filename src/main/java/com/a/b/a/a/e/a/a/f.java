/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.e.a.a;

import com.a.b.a.a.a.b;
import com.a.b.a.a.c.A;
import com.a.b.a.a.c.B;
import com.a.b.a.a.c.C;
import com.a.b.a.a.c.D;
import com.a.b.a.a.c.E;
import com.a.b.a.a.c.c;
import com.a.b.a.a.c.e;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.m;
import com.a.b.a.a.c.n;
import com.a.b.a.a.c.o;
import com.a.b.a.a.c.p;
import com.a.b.a.a.c.q;
import com.a.b.a.a.c.r;
import com.a.b.a.a.c.s;
import com.a.b.a.a.c.t;
import com.a.b.a.a.c.u;
import com.a.b.a.a.c.v;
import com.a.b.a.a.c.w;
import com.a.b.a.a.c.y;
import com.a.b.a.a.c.z;
import com.a.b.a.a.e.a.a.d;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.lang3.ObjectUtils;

public class f
extends d {
    private volatile int a;
    private volatile B[] b;

    public f(b b2, File file) {
        super(b2, file);
    }

    @Override
    public String b() {
        f.a(this.a(a.class), a.c);
        return this.h();
    }

    @Override
    public int c() {
        f.a(this.a(a.class), a.e);
        this.a = this.j();
        return this.a;
    }

    @Override
    public void b(int n2) {
        this.a(a.d);
        this.c(n2);
        this.m();
    }

    @Override
    public void a(m m2) {
        this.a(a.f);
        if (m2 == null) {
            this.b(false);
            return;
        }
        this.b(true);
        this.a(m2.getRandomSeed());
        this.c(m2.getTickCount());
        this.a(m2.getMapSize());
        this.b(m2.isSkillsEnabled());
        this.b(m2.isRawMessagesEnabled());
        this.a(m2.getFriendlyFireDamageFactor());
        this.a(m2.getBuildingDamageScoreFactor());
        this.a(m2.getBuildingEliminationScoreFactor());
        this.a(m2.getMinionDamageScoreFactor());
        this.a(m2.getMinionEliminationScoreFactor());
        this.a(m2.getWizardDamageScoreFactor());
        this.a(m2.getWizardEliminationScoreFactor());
        this.a(m2.getTeamWorkingScoreFactor());
        this.c(m2.getVictoryScore());
        this.a(m2.getScoreGainRange());
        this.c(m2.getRawMessageMaxLength());
        this.a(m2.getRawMessageTransmissionSpeed());
        this.a(m2.getWizardRadius());
        this.a(m2.getWizardCastRange());
        this.a(m2.getWizardVisionRange());
        this.a(m2.getWizardForwardSpeed());
        this.a(m2.getWizardBackwardSpeed());
        this.a(m2.getWizardStrafeSpeed());
        this.c(m2.getWizardBaseLife());
        this.c(m2.getWizardLifeGrowthPerLevel());
        this.c(m2.getWizardBaseMana());
        this.c(m2.getWizardManaGrowthPerLevel());
        this.a(m2.getWizardBaseLifeRegeneration());
        this.a(m2.getWizardLifeRegenerationGrowthPerLevel());
        this.a(m2.getWizardBaseManaRegeneration());
        this.a(m2.getWizardManaRegenerationGrowthPerLevel());
        this.a(m2.getWizardMaxTurnAngle());
        this.c(m2.getWizardMaxResurrectionDelayTicks());
        this.c(m2.getWizardMinResurrectionDelayTicks());
        this.c(m2.getWizardActionCooldownTicks());
        this.c(m2.getStaffCooldownTicks());
        this.c(m2.getMagicMissileCooldownTicks());
        this.c(m2.getFrostBoltCooldownTicks());
        this.c(m2.getFireballCooldownTicks());
        this.c(m2.getHasteCooldownTicks());
        this.c(m2.getShieldCooldownTicks());
        this.c(m2.getMagicMissileManacost());
        this.c(m2.getFrostBoltManacost());
        this.c(m2.getFireballManacost());
        this.c(m2.getHasteManacost());
        this.c(m2.getShieldManacost());
        this.c(m2.getStaffDamage());
        this.a(m2.getStaffSector());
        this.a(m2.getStaffRange());
        this.a((Object)m2.getLevelUpXpValues(), true);
        this.a(m2.getMinionRadius());
        this.a(m2.getMinionVisionRange());
        this.a(m2.getMinionSpeed());
        this.a(m2.getMinionMaxTurnAngle());
        this.c(m2.getMinionLife());
        this.c(m2.getFactionMinionAppearanceIntervalTicks());
        this.c(m2.getOrcWoodcutterActionCooldownTicks());
        this.c(m2.getOrcWoodcutterDamage());
        this.a(m2.getOrcWoodcutterAttackSector());
        this.a(m2.getOrcWoodcutterAttackRange());
        this.c(m2.getFetishBlowdartActionCooldownTicks());
        this.a(m2.getFetishBlowdartAttackRange());
        this.a(m2.getFetishBlowdartAttackSector());
        this.a(m2.getBonusRadius());
        this.c(m2.getBonusAppearanceIntervalTicks());
        this.c(m2.getBonusScoreAmount());
        this.a(m2.getDartRadius());
        this.a(m2.getDartSpeed());
        this.c(m2.getDartDirectDamage());
        this.a(m2.getMagicMissileRadius());
        this.a(m2.getMagicMissileSpeed());
        this.c(m2.getMagicMissileDirectDamage());
        this.a(m2.getFrostBoltRadius());
        this.a(m2.getFrostBoltSpeed());
        this.c(m2.getFrostBoltDirectDamage());
        this.a(m2.getFireballRadius());
        this.a(m2.getFireballSpeed());
        this.a(m2.getFireballExplosionMaxDamageRange());
        this.a(m2.getFireballExplosionMinDamageRange());
        this.c(m2.getFireballExplosionMaxDamage());
        this.c(m2.getFireballExplosionMinDamage());
        this.a(m2.getGuardianTowerRadius());
        this.a(m2.getGuardianTowerVisionRange());
        this.a(m2.getGuardianTowerLife());
        this.a(m2.getGuardianTowerAttackRange());
        this.c(m2.getGuardianTowerDamage());
        this.c(m2.getGuardianTowerCooldownTicks());
        this.a(m2.getFactionBaseRadius());
        this.a(m2.getFactionBaseVisionRange());
        this.a(m2.getFactionBaseLife());
        this.a(m2.getFactionBaseAttackRange());
        this.c(m2.getFactionBaseDamage());
        this.c(m2.getFactionBaseCooldownTicks());
        this.c(m2.getBurningDurationTicks());
        this.c(m2.getBurningSummaryDamage());
        this.c(m2.getEmpoweredDurationTicks());
        this.a(m2.getEmpoweredDamageFactor());
        this.c(m2.getFrozenDurationTicks());
        this.c(m2.getHastenedDurationTicks());
        this.a(m2.getHastenedBonusDurationFactor());
        this.a(m2.getHastenedMovementBonusFactor());
        this.a(m2.getHastenedRotationBonusFactor());
        this.c(m2.getShieldedDurationTicks());
        this.a(m2.getShieldedBonusDurationFactor());
        this.a(m2.getShieldedDirectDamageAbsorptionFactor());
        this.a(m2.getAuraSkillRange());
        this.a(m2.getRangeBonusPerSkillLevel());
        this.c(m2.getMagicalDamageBonusPerSkillLevel());
        this.c(m2.getStaffDamageBonusPerSkillLevel());
        this.a(m2.getMovementBonusFactorPerSkillLevel());
        this.c(m2.getMagicalDamageAbsorptionPerSkillLevel());
        this.m();
    }

    @Override
    public void a(u u2, boolean bl) {
        this.a(a.g);
        if (u2 == null) {
            this.b(false);
            return;
        }
        this.b(true);
        this.a(u2.getWizards());
        this.a(u2.getWorld(), bl);
        this.m();
    }

    @Override
    public s[] d() {
        f.a(this.a(a.class), a.h);
        int n2 = this.j();
        if (n2 < 0) {
            return null;
        }
        s[] arrs = new s[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            s s2;
            if (!this.i()) continue;
            arrs[i2] = s2 = new s();
            s2.setSpeed(this.l());
            s2.setStrafeSpeed(this.l());
            s2.setTurn(this.l());
            s2.setAction(this.a(com.a.b.a.a.c.a.class));
            s2.setCastAngle(this.l());
            s2.setMinCastDistance(this.l());
            s2.setMaxCastDistance(this.l());
            s2.setStatusTargetId(this.k());
            s2.setSkillToLearn(this.a(y.class));
            s2.setMessages(this.o());
        }
        return arrs;
    }

    private p[] o() {
        int n2 = this.j();
        if (n2 < 0) {
            return null;
        }
        p[] arrp = new p[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!this.i()) continue;
            arrp[i2] = new p(ObjectUtils.defaultIfNull(this.a(n.class), n.MIDDLE), this.a(y.class), this.a(false));
        }
        return arrp;
    }

    @Override
    public void e() {
        try {
            this.a(a.b);
            this.m();
        }
        catch (RuntimeException runtimeException) {
            // empty catch block
        }
    }

    private void a(E e2, boolean bl) {
        if (e2 == null) {
            this.b(false);
            return;
        }
        this.b(true);
        this.c(e2.getTickIndex());
        this.c(e2.getTickCount());
        this.a(e2.getWidth());
        this.a(e2.getHeight());
        this.a(e2.getPlayersUnsafe());
        this.a(e2.getWizardsUnsafe());
        this.a(e2.getMinionsUnsafe());
        this.a(e2.getProjectilesUnsafe());
        this.a(e2.getBonusesUnsafe());
        this.a(e2.getBuildingsUnsafe());
        this.a(e2.getTreesUnsafe());
    }

    private void a(t[] arrt) {
        this.a(arrt, this::a);
    }

    private void a(t t2) {
        if (t2 == null) {
            this.b(false);
        } else {
            this.b(true);
            this.a(t2.getId());
            this.b(t2.isMe());
            this.a(t2.getName());
            this.b(t2.isStrategyCrashed());
            this.c(t2.getScore());
            this.a(t2.getFaction());
        }
    }

    private void a(D[] arrd) {
        this.a(arrd, this::a);
    }

    private void a(D d2) {
        if (d2 == null) {
            this.b(false);
        } else {
            this.b(true);
            this.a((o)d2);
            this.a(d2.getOwnerPlayerId());
            this.b(d2.isMe());
            this.c(d2.getMana());
            this.c(d2.getMaxMana());
            this.a(d2.getVisionRange());
            this.a(d2.getCastRange());
            this.c(d2.getXp());
            this.c(d2.getLevel());
            this.a(d2.getSkillsUnsafe(), true);
            this.c(d2.getRemainingActionCooldownTicks());
            this.a((Object)d2.getRemainingCooldownTicksByAction(), true);
            this.b(d2.isMaster());
            this.a(d2.getMessages(), this::a);
        }
    }

    private void a(q[] arrq) {
        this.a(arrq, this::a);
    }

    private void a(q q2) {
        if (q2 == null) {
            this.b(false);
        } else {
            this.b(true);
            this.a((o)q2);
            this.a(q2.getType());
            this.a(q2.getVisionRange());
            this.c(q2.getDamage());
            this.c(q2.getCooldownTicks());
            this.c(q2.getRemainingActionCooldownTicks());
        }
    }

    private void a(v[] arrv) {
        this.a(arrv, this::a);
    }

    private void a(v v2) {
        if (v2 == null) {
            this.b(false);
        } else {
            this.b(true);
            this.a((com.a.b.a.a.c.f)v2);
            this.a(v2.getType());
            this.a(v2.getOwnerUnitId());
            this.a(v2.getOwnerPlayerId());
        }
    }

    private void a(com.a.b.a.a.c.b[] arrb) {
        this.a(arrb, this::a);
    }

    private void a(com.a.b.a.a.c.b b2) {
        if (b2 == null) {
            this.b(false);
        } else {
            this.b(true);
            this.a((com.a.b.a.a.c.f)b2);
            this.a(b2.getType());
        }
    }

    private void a(com.a.b.a.a.c.d[] arrd) {
        this.a(arrd, this::a);
    }

    private void a(com.a.b.a.a.c.d d2) {
        if (d2 == null) {
            this.b(false);
        } else {
            this.b(true);
            this.a((o)d2);
            this.a(d2.getType());
            this.a(d2.getVisionRange());
            this.a(d2.getAttackRange());
            this.c(d2.getDamage());
            this.c(d2.getCooldownTicks());
            this.c(d2.getRemainingActionCooldownTicks());
        }
    }

    private void a(B[] arrb) {
        if (arrb == null) {
            this.b = null;
        } else {
            int n2 = arrb.length;
            B[] arrb2 = new B[n2];
            System.arraycopy(arrb, 0, arrb2, 0, n2);
            Arrays.sort(arrb2, (b2, b3) -> Long.compare(b2.getId(), b3.getId()));
            if (Objects.deepEquals(arrb2, this.b)) {
                this.c(-1);
                return;
            }
            this.b = arrb2;
        }
        this.a(arrb, this::a);
    }

    private void a(B b2) {
        if (b2 == null) {
            this.b(false);
        } else {
            this.b(true);
            this.a((o)b2);
        }
    }

    private void a(p p2) {
        if (p2 == null) {
            this.b(false);
        } else {
            this.b(true);
            this.a(p2.getLane());
            this.a(p2.getSkillToLearn());
            this.a((Object)p2.getRawMessage(), true);
        }
    }

    private void a(z[] arrz) {
        this.a(arrz, this::a);
    }

    private void a(z z2) {
        if (z2 == null) {
            this.b(false);
        } else {
            this.b(true);
            this.a(z2.getId());
            this.a(z2.getType());
            this.a(z2.getWizardId());
            this.a(z2.getPlayerId());
            this.c(z2.getRemainingDurationTicks());
        }
    }

    private void a(o o2) {
        this.a((com.a.b.a.a.c.f)o2);
        this.c(o2.getLife());
        this.c(o2.getMaxLife());
        this.a(o2.getStatuses());
    }

    private void a(com.a.b.a.a.c.f f2) {
        this.a((C)f2);
        this.a(f2.getRadius());
    }

    private void a(C c2) {
        this.a(c2.getId());
        this.a(c2.getX());
        this.a(c2.getY());
        this.a(c2.getSpeedX());
        this.a(c2.getSpeedY());
        this.a(c2.getAngle());
        this.a(c2.getFaction());
    }

    private static void a(a a2, a a3) {
        if (a2 != a3) {
            throw new com.a.b.a.a.e.a.f(String.format("Received wrong message [actual=%s, expected=%s].", new Object[]{a2, a3}));
        }
    }

    private static final class a
    extends Enum<a> {
        public static final /* enum */ a a = new a();
        public static final /* enum */ a b = new a();
        public static final /* enum */ a c = new a();
        public static final /* enum */ a d = new a();
        public static final /* enum */ a e = new a();
        public static final /* enum */ a f = new a();
        public static final /* enum */ a g = new a();
        public static final /* enum */ a h = new a();
        private static final /* synthetic */ a[] $VALUES;

        public static a[] values() {
            return (a[])$VALUES.clone();
        }

        private a() {
            super(string, n2);
        }

        static {
            $VALUES = new a[]{a, b, c, d, e, f, g, h};
        }
    }

}

