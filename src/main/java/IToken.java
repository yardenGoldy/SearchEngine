import javafx.util.Pair;

import java.util.List;

public interface IToken {
    ParsedResult TryParse(List<String> sentence);
}
