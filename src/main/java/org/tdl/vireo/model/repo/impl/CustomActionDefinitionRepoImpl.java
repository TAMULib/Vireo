package org.tdl.vireo.model.repo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.tdl.vireo.model.CustomActionDefinition;
import org.tdl.vireo.model.repo.CustomActionDefinitionRepo;
import org.tdl.vireo.model.repo.custom.CustomActionDefinitionRepoCustom;
import org.tdl.vireo.service.OrderedEntityService;

public class CustomActionDefinitionRepoImpl implements CustomActionDefinitionRepoCustom {
    
    @Autowired
    private OrderedEntityService orderedEntityService;

    @Autowired
    private CustomActionDefinitionRepo customActionDefinitionRepo;

    @Override
    public CustomActionDefinition create(String label, Boolean isStudentVisible) {
        CustomActionDefinition customActionDefinition = new CustomActionDefinition(label, isStudentVisible);
        customActionDefinition.setPosition(customActionDefinitionRepo.count() + 1);
        return customActionDefinitionRepo.save(customActionDefinition);
    }
    
    @Override
    public void reorder(Long src, Long dest) {
        orderedEntityService.reorder(CustomActionDefinition.class, src, dest);
    }
    
    @Override
    public void remove(Long index) {
        orderedEntityService.remove(CustomActionDefinition.class, index);
    }
}
