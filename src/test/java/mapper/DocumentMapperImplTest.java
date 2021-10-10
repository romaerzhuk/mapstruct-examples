package mapper;

import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.doReturn;
import static test.extension.UidExtension.newEnum;
import static test.extension.UidExtension.uidD;
import static test.extension.UidExtension.uidDec;
import static test.extension.UidExtension.uidL;
import static test.extension.UidExtension.uidS;

import dto.AccountDto;
import dto.DocumentDto;
import dto.DocumentStatus;
import dto.StatusDto;
import entity.Account;
import entity.Document;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import test.extension.UidExtension;
import test.hamcrest.PropertiesMatcher;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

/**
 * DocumentMapperImplTest.
 *
 * @author Roman_Erzhukov
 */
@ExtendWith({
        MockitoExtension.class,
        UidExtension.class
})
class DocumentMapperImplTest {

    DocumentMapperImpl subj = new DocumentMapperImpl();

    @Spy
    @InjectMocks
    DocumentMapperImpl_ delegate;

    @Mock
    AccountMapper accountMapper;

    @BeforeEach
    void init() {
        subj.setDelegate(delegate);
    }

    @ParameterizedTest
    @MethodSource("toDtoArguments")
    void toDto(Document expected) {
        List<AccountDto> expectedAccounts = expected == null ? null : List.of(newAccountDto(), newAccountDto());
        if (expected != null) {
            doReturn(expectedAccounts).when(accountMapper).toDtoList(expected.getAccounts());
        }

        DocumentDto actual = subj.toDto(expected);

        assertThat(actual, documentDto(expected, expectedAccounts));
    }

    static Stream<Document> toDtoArguments() {
        return Stream.concat(Stream.of(null,
                new Document(),
                newDocument()
                        .setAccounts(null)),
                Stream.of(DocumentStatus.values())
                        .map(status -> newDocument()
                                .setStatus(status)));
    }

    static Document newDocument() {
        return new Document()
                .setId(uidL())
                .setAmount(uidDec())
                .setStatus(newEnum(DocumentStatus.class))
                .setAccounts(List.of(newAccount(), newAccount()));
    }

    static Account newAccount() {
        return new Account()
                .setId(uidL())
                .setBic(uidS())
                .setAccountNumber(uidS());
    }

    Matcher<DocumentDto> documentDto(Document expected, List<AccountDto> expectedAccounts) {
        if (expected == null) {
            return nullValue(DocumentDto.class);
        }
        return PropertiesMatcher.of(DocumentDto.class, (actual, matcher) -> matcher
                .add("id", actual.getId(), expected.getId())
                .add("amount", actual.getAmount(), ofNullable(expected.getAmount())
                        .map(BigDecimal::doubleValue)
                        .orElse(null))
                .add("status", actual.getStatus(), expected.getStatus(), (a, e) -> matcher
                        .add("system", a.getSystem(), e)
                        .add("name", a.getName(), name(e)))
                .add("accounts", actual.getAccounts(), expectedAccounts));
    }

    String name(DocumentStatus status) {
        switch (status) {
            case NEW:
                return "Новый";
            case DRAFT:
                return "Черновик";
            case SIGNED:
                return "Подписан";
            case EXECUTED:
                return "Исполнен";
            default:
                throw new IllegalArgumentException("status=" + status);
        }
    }

    @ParameterizedTest
    @MethodSource("toEntityArguments")
    void toEntity(DocumentDto expected) {
        List<Account> expectedAccounts = expected == null ? null : List.of(newAccount(), newAccount());
        if (expected != null) {
            doReturn(expectedAccounts).when(accountMapper).toEntities(expected.getAccounts());
        }

        assertThat(subj.toEntity(expected), document(expected, expectedAccounts));
    }

    static Stream<DocumentDto> toEntityArguments() {
        return Stream.of(null,
                new DocumentDto(),
                newDocumentDto(),
                newDocumentDto()
                        .setAccounts(null)
                        .setStatus(null)
        );
    }

    static DocumentDto newDocumentDto() {
        return new DocumentDto()
                .setId(uidL())
                .setAmount(uidD())
                .setStatus(new StatusDto()
                        .setSystem(newEnum(DocumentStatus.class))
                        .setName(uidS()))
                .setAccounts(List.of(newAccountDto(), newAccountDto()));
    }

    static AccountDto newAccountDto() {
        return new AccountDto()
                .setId(uidL())
                .setBic(uidS())
                .setAccountNumber(uidS());
    }

    Matcher<Document> document(DocumentDto expected, List<Account> expectedAccounts) {
        if (expected == null) {
            return nullValue(Document.class);
        }
        return PropertiesMatcher.of(Document.class, (actual, matcher) -> matcher
                .add("id", actual.getId(), expected.getId())
                .add("amount", actual.getAmount(), ofNullable(expected.getAmount())
                        .map(BigDecimal::valueOf)
                        .orElse(null))
                .add("status", actual.getStatus(), ofNullable(expected.getStatus())
                        .map(StatusDto::getSystem)
                        .orElse(null))
                .add("accounts", actual.getAccounts(), expectedAccounts));
    }
}