package dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * StatusDto.
 *
 * @author Roman_Erzhukov
 */
@Data
@Accessors(chain = true)
public class StatusDto {
    private DocumentStatus system;
    private String name;
}
