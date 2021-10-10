package dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * DocumentDto.
 *
 * @author Roman_Erzhukov
 */
@Data
@Accessors(chain = true)
public class DocumentDto {
    private Long id;
    private Double amount;
    private List<AccountDto> accounts;
    private StatusDto status;
}
