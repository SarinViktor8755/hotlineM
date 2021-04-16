package com.mygdx.game.Characters.Animation;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MainGaming;

import java.util.HashMap;

public class AnimationPers {
    private MainGaming mainGaming;
    private TextureAtlas legs;

    private HashMap<Integer, TextureRegion> steps;
    private HashMap<Integer, TextureRegion> vest;
    private HashMap<Integer, TextureRegion> kick_corpse;
    private HashMap<Integer, TextureRegion> pistol;
    private HashMap<Integer, TextureRegion> shotGun;

    public TextureRegion bullet;


    public AnimationPers(MainGaming mainGaming) {
        this.mainGaming = mainGaming;
        this.steps = new HashMap<Integer, TextureRegion>(); // текстуры ног
        this.vest = new HashMap<Integer, TextureRegion>(); // ;жилет -цвет куртки
        this.kick_corpse = new HashMap<Integer, TextureRegion>(); // удары выстрелы ...
        this.pistol = new HashMap<Integer, TextureRegion>(); // удары выстрелы ...
        this.shotGun = new HashMap<Integer, TextureRegion>(); // шотган




        //mainGaming.getMainClient().getMyIdConnect();
        legs = mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class);


        for (int i = 0; i <= 5; i++) {
            steps.put(i, legs.findRegion("shag" + i));
        }

//        for (int i = 0; i <= 5; i++) {
//            vest.put(i, legs.findRegion("vest" + i));
//        }

        for (int i = 0; i <= 9; i++) {
            kick_corpse.put(i, legs.findRegion("tr" + i));
        }
        for (int i = 0; i <= 5; i++) {
            kick_corpse.put(i + 4, legs.findRegion("hit" + i));
        }

        for (int i = 0; i <= 3; i++) {
            pistol.put(i, legs.findRegion("pistol" + i));
        }

        for (int i = 0; i <= 5; i++) {
            shotGun.put(i, legs.findRegion("shotgun" + i));
        }

        ///////////////////////
        vest.put(1, legs.findRegion("hit1t"));
        vest.put(2, vest.get(1));
        vest.put(3, legs.findRegion("hit3t"));
        vest.put(4, legs.findRegion("hit4t"));
        vest.put(5, legs.findRegion("hit5t"));
        ///////////////////////
        vest.put(6, legs.findRegion("pistol1t"));
        vest.put(7, vest.get(6));
        vest.put(8, legs.findRegion("pistol3t"));
        ///////////////////////
        vest.put(9, legs.findRegion("shotgun1t"));
        vest.put(10, legs.findRegion("shotgun2t"));
        vest.put(11, vest.get(10));
        vest.put(12, vest.get(10));


    }

    public TextureRegion getTextureLegsFromId(int idPers) {
        float per = getSpeed(mainGaming.getHero().getOtherPlayers().getSpeedPlayer(idPers));
        if (mainGaming.getHero().getOtherPlayers().getTimerCurrenCycleDromPlayer(idPers) > per) {
            mainGaming.getHero().getOtherPlayers().setTacktPlayer(idPers, mainGaming.getHero().getOtherPlayers().getTacktPlayer(idPers) + 1);
            mainGaming.getHero().getOtherPlayers().setTimerCurrenCycleDromPlayer(idPers, 0);
            if (mainGaming.getHero().getOtherPlayers().getTacktPlayer(idPers) > 20)
                mainGaming.getHero().getOtherPlayers().setTacktPlayer(idPers, 0);
        }
        if (per < .5f)
            mainGaming.getAudioEngine().addNewSoundStepToPleyerFromID(idPers); //добавляем еденицу шага в зависимости от звука
        if ((mainGaming.getMainClient().myIdConnect == idPers) && (mainGaming.getHero().getVelocity().x == 0) && (mainGaming.getHero().getVelocity().y == 0))
            return flipTextNormalY(steps.get(0));
        int cadr = mainGaming.getHero().getOtherPlayers().getTacktPlayer(idPers);
        if (cadr == 1) return flipTextNormalY(steps.get(0));
        if (cadr == 2) return flipTextNormalY(steps.get(1));
        if (cadr == 3) return flipTextNormalY(steps.get(2));
        if (cadr == 4) return flipTextNormalY(steps.get(3));
        if (cadr == 5) return flipTextNormalY(steps.get(4));
        if (cadr == 6) return flipTextNormalY(steps.get(5));
        if (cadr == 7) return flipTextNormalY(steps.get(4));
        if (cadr == 8) return flipTextNormalY(steps.get(3));
        if (cadr == 9) return flipTextNormalY(steps.get(2));
        if (cadr == 10) return flipTextNormalY(steps.get(1));
        if (cadr == 11) return flipTextIvertY(steps.get(0));
        if (cadr == 12) return flipTextIvertY(steps.get(1));
        if (cadr == 13) return flipTextIvertY(steps.get(2));
        if (cadr == 14) return flipTextIvertY(steps.get(3));
        if (cadr == 15) return flipTextIvertY(steps.get(4));
        if (cadr == 16) return flipTextIvertY(steps.get(5));
        if (cadr == 17) return flipTextIvertY(steps.get(4));
        if (cadr == 18) return flipTextIvertY(steps.get(3));
        if (cadr == 19) return flipTextIvertY(steps.get(2));
        if (cadr == 20) return flipTextIvertY(steps.get(1));
        return steps.get(0);
    }

    private int getIndexWeapon(int id) { // взять номер анимации по играку
        if (id == mainGaming.getMainClient().getMyIdConnect())
            return mainGaming.getHero().getWeapons().getWeapon();
        return mainGaming.getHero().getOtherPlayers().getPlayerToID(id).getWeapons();
    }

    public TextureRegion getTextureBodyFromId(int id) { // метод тело
        int w = getIndexWeapon(id);
        return decipheringCodeAnimation(mainGaming.getHero().getOtherPlayers().getPlayerToID(id).getAnimatonBody().getNomberanimation(), mainGaming.getHero().getOtherPlayers().getPlayerToID(id).getAnimatonBody().isFlip(), w);
    }

    public TextureRegion getTextureVestFromId(int id, int weapon) {
        return getVestColor(mainGaming.getHero().getOtherPlayers().getPlayerToID(id).getAnimatonBody().getNomberanimation(), mainGaming.getHero().getOtherPlayers().getPlayerToID(id).getAnimatonBody().isFlip(), weapon);
    }

    public boolean isRedyToAttack() {
        try {
            int sizeA = mainGaming.getHero().getOtherPlayers().getPlayerToID(mainGaming.getMainClient().getMyIdConnect()).getAnimatonBody().getAnimationBodyFreme().size();
            if (sizeA > 0) return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    private TextureRegion decipheringCodeAnimation(int idAnimation, boolean flip, int weapon) { //расшифраовать номер спрайта
        if (weapon == 1) return getAnimationBodyFromStick(idAnimation, flip);
        if (weapon == 2) return getAnimationBodyFromPistols(idAnimation);
        if (weapon == 3) return getAnimationBodyFromShotGun(idAnimation);
        return null;
    }


    private TextureRegion getAnimationBodyFromStick(int idAnimation, boolean flip) {

        if (idAnimation == 1) {
            if (flip) {
                return flipTextIvertY(kick_corpse.get(5));
            } else return flipTextNormalY(kick_corpse.get(5));
        }

        if (idAnimation == 2) {
            if (flip) {
                return flipTextIvertY(kick_corpse.get(6));
            } else return flipTextNormalY(kick_corpse.get(6));
        }

        if (idAnimation == 3) {
            if (flip) {
                return flipTextIvertY(kick_corpse.get(7));
            } else return flipTextNormalY(kick_corpse.get(7));
        }

        if (idAnimation == 4) {
            if (flip) {
                return flipTextIvertY(kick_corpse.get(8));
            } else return flipTextNormalY(kick_corpse.get(8));
        }

        if (idAnimation == 6) {
            if (flip) {
                return flipTextIvertY(kick_corpse.get(1));
            } else return flipTextNormalY(kick_corpse.get(2));
        }

        if (idAnimation == 7) {
            if (flip) {
                return flipTextIvertY(kick_corpse.get(2));
            } else return flipTextNormalY(kick_corpse.get(2));
        }

        if (idAnimation == 8) {
            if (flip) {
                return flipTextIvertY(kick_corpse.get(3));
            } else return flipTextNormalY(kick_corpse.get(3));
        }

        if (idAnimation == -1) {
            if (flip) {
                return flipTextIvertY(kick_corpse.get(5));
            } else return flipTextNormalY(kick_corpse.get(5));
        }
        return kick_corpse.get(5);
    }

    private TextureRegion getAnimationBodyFromPistols(int idAnimation) {
        return pistol.get(idAnimation);

    }

    private TextureRegion getAnimationBodyFromShotGun(int idAnimation) {
        return shotGun.get(idAnimation);

    }


    public TextureRegion getVestColor(int idAnimation, boolean flip, int weapon) {
        // System.out.println(idAnimation);
        if (weapon == 1) {
            if (idAnimation == 1) {
                if (flip) {
                    return flipTextIvertY(vest.get(idAnimation));
                } else return flipTextNormalY(vest.get(idAnimation));
            }
        }
        if (weapon == 2) {
            return  vest.get(idAnimation + 5 );
        }
        if (weapon == 3) {
            return  vest.get(idAnimation + 8);
        }


        if (idAnimation == 1) {
            if (flip) {
                return flipTextIvertY(vest.get(idAnimation));
            } else return flipTextNormalY(vest.get(idAnimation));
        }

        if (idAnimation == 2) {
            if (flip) {
                return flipTextIvertY(vest.get(idAnimation));
            } else return flipTextNormalY(vest.get(idAnimation));
        }


        if (idAnimation == 3) {
            if (flip) {
                return flipTextIvertY(vest.get(idAnimation));
            } else return flipTextNormalY(vest.get(idAnimation));
        }

        if (idAnimation == 4) {
            if (flip) {
                return flipTextIvertY(vest.get(idAnimation));
            } else return flipTextNormalY(vest.get(idAnimation));
        }
//
//        if (idAnimation == -1) {
//            if (flip) {
//                return flipTextIvertY(vest.get(1));
//            } else return flipTextNormalY(vest.get(1));
//        }
        return null;
    }

    private float getSpeed(float speed) {
        if (speed > 10) return .02f;
        if (speed > 6) return .7f;
        if (speed > 3) return 1;
        if (speed < 1) return Integer.MAX_VALUE;
        return 0;

    }

    private TextureRegion flipTextIvertY(TextureRegion in) {
        if (in.isFlipY()) in.flip(false, true);
        return in;
    }


    private TextureRegion flipTextNormalY(TextureRegion in) {
        if (!in.isFlipY()) in.flip(false, true);
        return in;
    }


    private void flipTextReg(TextureRegion in) {
        if (in.isFlipY()) in.flip(false, false);
        else in.flip(false, true);
    }

    private void flipTextReg(TextureRegion in, boolean logik) {
        if (logik == in.isFlipY()) return;
        if (logik != in.isFlipY()) {
            in.flip(false, true);
        }
    }

    private boolean invert(boolean in) {
        if (in) return false;
        else return true;
    }

    @Override
    public String toString() {
        return "AnimationPers{" +
                "mainGaming=" + mainGaming +
                ", legs=" + legs +
                ", steps=" + steps +
                ", vest=" + vest +
                ", kick_corpse=" + kick_corpse +
                ", pistol=" + pistol +
                '}';
    }


}
