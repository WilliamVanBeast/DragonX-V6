package net.shadow.client.util;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class JDKUtils {

	// java.util.regex.Pattern.asMatchPredicate()Ljava/util/function/Predicate;
	// does not exist in teavm for some reason
	public static Predicate<String> asMatchPredicate(Pattern pattern) {
		return s -> pattern.matcher(s).matches();
	}

}
