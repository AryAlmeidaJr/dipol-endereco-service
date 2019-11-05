package br.gov.sp.prodesp.ssp.dipol.enderecoservice.util;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static boolean areNotBlanks(String... strings) {
		for (String string : strings) {
			if (StringUtils.isBlank(string)) {
				return false;
			}
		}

		return true;
	}

}
