package mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static test.extension.UidExtension.uidL;
import static test.extension.UidExtension.uidS;

import dto.AccountDto;
import entity.Account;
import entity.AccountService;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import test.extension.UidExtension;
import test.hamcrest.PropertiesMatcher;

import java.util.stream.Stream;

/**
 * AccountMapperImplTest.
 *
 * @author Roman_Erzhukov
 */
@ExtendWith({
        MockitoExtension.class,
        UidExtension.class
})
class AccountMapperImplTest {

    @InjectMocks
    AccountMapperImpl subj;

    @Spy
    @SuppressWarnings("unused")
    AccountMapperImpl_ delegate;

    @Mock
    AccountService service;

    @ParameterizedTest
    @MethodSource("toDtoArguments")
    void toDto(Account expected) {
        String userName = uidS();
        lenient().doReturn(userName).when(service).userName(expected);

        AccountDto actual = subj.toDto(expected);

        assertThat(actual, accountDto(expected, userName));
        verify(service, times(expected == null ? 0 : 1)).userName(any());
    }

    static Stream<Account> toDtoArguments() {
        return Stream.of(null,
                new Account(),
                new Account()
                        .setId(uidL())
                        .setBic(uidS())
                        .setAccountNumber(uidS()));
    }

    Matcher<AccountDto> accountDto(Account expected, String userName) {
        if (expected == null) {
            return nullValue(AccountDto.class);
        }
        return PropertiesMatcher.of(AccountDto.class, (actual, matcher) -> matcher
                .add("id", actual.getId(), expected.getId())
                .add("bic", actual.getBic(), expected.getBic())
                .add("accountNumber", actual.getAccountNumber(), expected.getAccountNumber())
                .add("userName", actual.getUserName(), userName));
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