package com.mig.sales.api.model.mapper;

import com.mig.sales.api.model.dto.LeadDTO;
import com.mig.sales.api.model.dto.CreateLeadRequest;
import com.mig.sales.api.model.dto.UpdateLeadRequest;
import com.mig.sales.api.model.entity.Lead;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * MapStruct mapper for Lead entity and DTOs.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface LeadMapper {

    /**
     * Map Lead entity to LeadDTO.
     */
    LeadDTO toDTO(Lead lead);

    /**
     * Map LeadDTO to Lead entity.
     */
    @Mapping(target = "history", ignore = true)
    Lead toEntity(LeadDTO leadDTO);

    /**
     * Map CreateLeadRequest to Lead entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "NEW")
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "leadScore", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "pegaWorkflowId", ignore = true)
    @Mapping(target = "pegaCaseId", ignore = true)
    @Mapping(target = "pegaStatus", ignore = true)
    @Mapping(target = "pegaLastSyncDate", ignore = true)
    @Mapping(target = "history", ignore = true)
    Lead toEntity(CreateLeadRequest request);

    /**
     * Map UpdateLeadRequest to Lead entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "pegaWorkflowId", ignore = true)
    @Mapping(target = "pegaCaseId", ignore = true)
    @Mapping(target = "pegaStatus", ignore = true)
    @Mapping(target = "pegaLastSyncDate", ignore = true)
    @Mapping(target = "history", ignore = true)
    Lead toEntity(UpdateLeadRequest request);

    /**
     * Map list of Lead entities to list of LeadDTOs.
     */
    List<LeadDTO> toDTOList(List<Lead> leads);

    /**
     * Update Lead entity with values from UpdateLeadRequest.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "pegaWorkflowId", ignore = true)
    @Mapping(target = "pegaCaseId", ignore = true)
    @Mapping(target = "pegaStatus", ignore = true)
    @Mapping(target = "pegaLastSyncDate", ignore = true)
    @Mapping(target = "history", ignore = true)
    void updateEntity(UpdateLeadRequest request, @MappingTarget Lead lead);
}
