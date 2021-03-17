package com.mygdx.game.HUDAudio;

import com.mygdx.game.MainGaming;
import com.mygdx.game.Service.StaticService;

/**
 * Created by 1 on 28.03.2020.
 */

public class SoundTrack {
    private MainGaming mainGaming;
    private boolean startGame;
    private float timer;
    private float voiceTimer;
    private int frag;

    private boolean finalVoice;

    private boolean lidrVoice;

    private int position;

    public SoundTrack(MainGaming mainGaming) {
        this.mainGaming = mainGaming;
        startGame = false;
        timer = 0;
        position = 0;
        this.frag = 0;
        lidrVoice = true;
        finalVoice = false;
    }

    public void ubdate(float dt) {
        if (!mainGaming.getMainClient().isConnectToServer()) {
            timer = 0;
            this.startGame = false;
        } else
            timer += dt;
        voiceTimer += dt;
        pleyStart();

        //System.out.println(mainGaming.getHud().get);
        //if(timer < 3000) pleyFinal();
    }


    private void pleyFinal() {
        //if (finalVoice) return;
        //finalVoice = true;
        //if(mainGaming.getHud().getMyPosition() > 1) mainGaming.getAudioEngine().pleyYouLoose(); else mainGaming.getAudioEngine().pleyYouWin();
    }

    private void pleyStart() {
        if (!startGame && (timer > 1)) {
            mainGaming.getAudioEngine().pleyFight();
            startGame = true;
            voiceTimer = 0;
            mainGaming.getHero().getPoolBlood().startingAdStart();
        }

    }

    public void pleyLostPrimuschestvo(int position, int frag) {
        if (this.position == 1 && position != 1 && mainGaming.getHud().getMyFrags() > 2) { // больше не лидером
            mainGaming.getHero().getPoolBlood().startingAdLostLead();
          //  if (mainGaming.getHud().getMyFrags() > 1) {
                mainGaming.getAudioEngine().pleyLostLead();
                lidrVoice = false;
                voiceTimer = 0;
          //  }
            //return;
        }

        if (this.position != 1 && position == 1) { // стал лидером
            //System.out.println("Lider  " + lidrVoice +"  :: "+ (mainGaming.getHud().getMyFrags() > 0));
            if (!lidrVoice && mainGaming.getHud().getMyFrags() > 0 && mainGaming.getHud().getMyFrags() > 2) {
                mainGaming.getAudioEngine().pleyBestLead();
                lidrVoice = true;
                voiceTimer = 0;
               // mainGaming.getHero().getPoolBlood().startingAdFirst(); // ПЕРВЫЙ
            }
            //return;
        }

        if (this.frag < frag) { // прсто голос Дюка
            //System.out.println("DUKE1");
            if (voiceTimer > 3) {
               // System.out.println("DUKE2");
                mainGaming.getAudioEngine().pleyVoice();
                voiceTimer = 0;

            }
        }
        this.frag = frag;
        this.position = position;


    }

    public void pleyVoice() {
        //System.out.println("sound 1");
        if (voiceTimer < 5) return;
        if (timer < 3) return;
        //System.out.println("sound 2");
        if (StaticService.selectWithProbability(80)) {
            //System.out.println("sound 3");
            mainGaming.getAudioEngine().pleyVoice();
            this.voiceTimer = 0;
        }

    }


}
