package entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Account.
 *
 * @author Roman_Erzhukov
 */
@ToString
@Getter
@Setter
@Accessors(chain = true)
public class Account {
    private Long id;
    private String bic;
    private String accountNumber;
}
