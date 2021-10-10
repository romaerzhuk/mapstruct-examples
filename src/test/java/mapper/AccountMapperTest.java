package mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static test.extension.UidExtension.uidL;
import static test.extension.UidExtension.uidS;

import dto.AccountDto;
import entity.Account;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import test.extension.UidExtension;
import test.hamcrest.PropertiesMatcher;

import java.util.stream.Stream;

/**
 * AccountMapperTest.
 *
 * @author Roman_Erzhukov
 */
@ExtendWith(UidExtension.class)
class AccountMapperTest {

    AccountMapper subj = new AccountMapperImpl();

    @ParameterizedTest
    @MethodSource("toDtoArguments")
    void toDto(Account expected) {
        assertThat(subj.toDto(expected), accountDto(expected));
    }

    static Stream<Account> toDtoArguments() {
        return Stream.of(null,
                new Account(),
                new Account()
                        .setId(uidL())
                        .setBic(uidS())
                        .setAccountNumber(uidS()));
    }

    Matcher<AccountDto> accountDto(Account expected) {
        if (expected == null) {
            return nullValue(AccountDto.class);
        }
        return PropertiesMatcher.of(AccountDto.class, (actual, matcher) -> matcher
                .add("id", actual.getId(), expected.getId())
                .add("bic", actual.getBic(), expected.getBic())
                .add("accountNumber", actual.getAccountNumber(), expected.getAccountNumber()));
    }

    @ParameterizedTest
    @MethodSource("toEntityArguments")
    void toEntity(AccountDto expected) {
        assertThat(subj.toEntity(expected), account(expected));
    }

    static Stream<AccountDto> toEntityArguments() {
        return Stream.of(null,
                new AccountDto(),
                new AccountDto()
                        .setId(uidL())
                        .setBic(uidS())
                        .setAccountNumber(uidS()));
    }

    Matcher<Account> account(AccountDto expected) {
        if (expected == null) {
            return nullValue(Account.class);
        }
        return PropertiesMatcher.of(Account.class, (actual, matcher) -> matcher
                .add("id", actual.getId(), expected.getId())
                .add("bic", actual.getBic(), expected.getBic())
                .add("accountNumber", actual.getAccountNumber(), expected.getAccountNumber()));
    }
}