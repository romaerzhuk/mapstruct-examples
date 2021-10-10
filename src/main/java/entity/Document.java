package entity;

import dto.DocumentStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * Document.
 *
 * @author Roman_Erzhukov
 */
@ToString
@Getter
@Setter
@Accessors(chain = true)
public class Document {
    private Long id;
    private BigDecimal amount;
    private List<Account> accounts;
    private DocumentStatus status;
}
