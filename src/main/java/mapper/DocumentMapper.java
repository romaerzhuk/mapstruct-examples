package mapper;

import dto.DocumentDto;
import dto.DocumentStatus;
import dto.StatusDto;
import entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * DocumentMapper.
 *
 * @author Roman_Erzhukov
 */
@Mapper
public abstract class DocumentMapper {
    @Mapping(source = "status", target = "status", qualifiedByName = "toStatusDto")
    public abstract DocumentDto toDto(Document entity);

    @Named("toStatusDto")
    StatusDto toStatusDto(DocumentStatus status) {
        if (status == null) {
            return null;
        }
        return new StatusDto()
                .setSystem(status)
                .setName(statusName(status));
    }

    private String statusName(DocumentStatus status) {
        switch (status) {
            case NEW:
                return "Новый";
            case DRAFT:
                return "Черновик";
            case SIGNED:
                return "Подписан";
            default:
                return "Исполнен";
        }
    }

    @Mapping(target = "status", source = "status.system")
    public abstract Document toEntity(DocumentDto dto);
}
