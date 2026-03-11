package com.ggar.hibiki.core.orchestrator.mapper;

import com.ggar.hibiki.core.orchestrator.dto.DeviceDTO;
import com.ggar.hibiki.features.devices.model.Device;
import com.ggar.hibiki.features.devices.model.DeviceId;
import com.ggar.hibiki.features.devices.model.UserId;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DeviceOrchestratorMapper {

    @Mapping(target = "deviceId", source = "id")
    @Mapping(target = "friendlyName", source = "name")
    @Mapping(target = "platform", source = "type")
    @Mapping(target = "appVersion", ignore = true)
    @Mapping(
            target = "isActive",
            expression = "java(domain.getStatus() == com.ggar.hibiki.features.devices.model.DeviceStatus.ACTIVE)")
    DeviceDTO toDto(Device domain);

    List<DeviceDTO> toDtoList(List<Device> list);

    default UUID map(DeviceId value) {
        return value != null ? value.getValue() : null;
    }

    default UUID map(UserId value) {
        return value != null ? value.getValue() : null;
    }
}
