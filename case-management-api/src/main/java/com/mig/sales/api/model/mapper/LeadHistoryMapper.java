package com.mig.sales.api.model.mapper;

import com.mig.sales.api.model.dto.LeadHistoryDTO;
import com.mig.sales.api.model.dto.AddCommentRequest;
import com.mig.sales.api.model.entity.LeadHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * MapStruct mapper for LeadHistory entity and DTOs.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface LeadHistoryMapper {

    /**
     * Map LeadHistory entity to LeadHistoryDTO.
     */
    @Mapping(target = "leadId", source = "lead.id")
    LeadHistoryDTO toDTO(LeadHistory leadHistory);

    /**
     * Map LeadHistoryDTO to LeadHistory entity.
     */
    @Mapping(target = "lead", ignore = true)
    LeadHistory toEntity(LeadHistoryDTO leadHistoryDTO);

    /**
     * Map AddCommentRequest to LeadHistory entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lead", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    LeadHistory toEntity(AddCommentRequest request);

    /**
     * Map list of LeadHistory entities to list of LeadHistoryDTOs.
     */
    List<LeadHistoryDTO> toDTOList(List<LeadHistory> leadHistories);
}
