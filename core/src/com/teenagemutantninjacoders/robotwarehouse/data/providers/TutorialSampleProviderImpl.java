package com.teenagemutantninjacoders.robotwarehouse.data.providers;

import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.TutorialSampleProvider;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample_1b;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample_1c;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample_2a;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample_3a;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample_4a;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample_5a;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample_5b;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample_6a;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample_6b;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample_7a;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample_8a;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample_8b;

/**
 * Created by JordiM on 29/11/2017.
 */

public class TutorialSampleProviderImpl implements TutorialSampleProvider {
    @Override
    public TutorialSample getTutorialSample(String sampleId) {
        if(sampleId.equals("tutorial_anim1b")) return new TutorialSample_1b();
        else if(sampleId.equals("tutorial_anim1c")) return new TutorialSample_1c();
        else if(sampleId.equals("tutorial_anim12a")) return new TutorialSample_2a();
        else if(sampleId.equals("tutorial_anim13a")) return new TutorialSample_3a();
        else if(sampleId.equals("tutorial_anim14a")) return new TutorialSample_4a();
        else if(sampleId.equals("tutorial_anim15a")) return new TutorialSample_5a();
        else if(sampleId.equals("tutorial_anim15b")) return new TutorialSample_5b();
        else if(sampleId.equals("tutorial_anim16a")) return new TutorialSample_6a();
        else if(sampleId.equals("tutorial_anim16b")) return new TutorialSample_6b();
        else if(sampleId.equals("tutorial_anim17a")) return new TutorialSample_7a();
        else if(sampleId.equals("tutorial_anim18a")) return new TutorialSample_8a();
        else if(sampleId.equals("tutorial_anim18b")) return new TutorialSample_8b();
        return new TutorialSample_1b();
    }
}
