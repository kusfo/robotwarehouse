package com.teenagemutantninjacoders.robotwarehouse.data.mappers;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.tutorials.TutorialDataElementDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.TutorialSampleProvider;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.tutorials.TutorialDataElement;

/**
 * Created by JordiM on 13/11/2017.
 */

public class TutorialElementDTOtoTutorialElementMapper implements BaseDTOMapper<TutorialDataElementDTO,TutorialDataElement> {
    final TutorialSampleProvider tutorialSampleProvider;

    public TutorialElementDTOtoTutorialElementMapper(TutorialSampleProvider tutorialSampleProvider) {
        this.tutorialSampleProvider = tutorialSampleProvider;
    }

    @Override
    public TutorialDataElement transform(TutorialDataElementDTO tutorialDataElementDTO) {
        TutorialDataElement tutorialDataElement = new TutorialDataElement();
        tutorialDataElement.setBody(GlobalGeneralData.getInstance()
                .getTutorialBundleData().get(tutorialDataElementDTO.getLabeltext()));
        if(tutorialDataElementDTO.getSampleId() != null)
        {
            tutorialDataElement.setCurrentSample(tutorialSampleProvider.getTutorialSample(tutorialDataElementDTO.getSampleId()));
        } else {
            tutorialDataElement.setCurrentSample(null);
        }
        return tutorialDataElement;
    }
}
