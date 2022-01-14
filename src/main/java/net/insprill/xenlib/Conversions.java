package net.insprill.xenlib;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class Conversions {

	/**
	 * Attempts to convert an {@link Object} to a boolean.
	 *
	 * @param obj Object to convert.
	 * @param def Default value if conversion fails.
	 * @return A best-guess as to the Objects boolean value, or the default if none is found.
	 */
	public boolean toBoolean(Object obj, boolean def) {
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else if (obj instanceof String) {
			switch (obj.toString().toLowerCase(Locale.ROOT)) {
				case "true":
				case "enabled":
				case "yes":
				case "1":
					return true;
				case "false":
				case "disabled":
				case "no":
				case "0":
					return false;
			}
		} else if (obj instanceof Number) {
			return ((Number) obj).intValue() > 0;
		}
		return def;
	}

}
