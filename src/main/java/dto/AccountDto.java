package dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * AccountDto.
 *
 * @author Roman_Erzhukov
 */
@Data
@Accessors(chain = true)
public class AccountDto {
    private Long id;
    private String bic;
    private String accountNumber;
}
