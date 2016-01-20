package library;

import java.math.BigDecimal;
import java.math.BigInteger;

public enum Type {

	INTEGER(BigInteger.class), DECIMAL(BigDecimal.class), STRING(
			CharSequence.class), CHARACTER(Character.class), BOOLEAN(
					Boolean.class), ARRAY(ArrayList.class), FUNCTION(
							A_RayCode.class), OBJECT(Object.class);

	private final Class<?> c;

	private <T> Type(Class<T> c) {
		this.c = c;
	}

	public boolean isMatch(Object object) {
		switch (this) {
		case ARRAY:
		case BOOLEAN:
		case CHARACTER:
		case DECIMAL:
		case INTEGER:
		case STRING:
		case FUNCTION:
		case OBJECT:
			return c.isInstance(object);
		}
		return false;
	}

	public Class<?> getClassOfType() {
		return c;
	}

	public static Type getMatch(Object object) {
		for (Type type : Type.values()) {
			if (type.isMatch(object)) {
				return type;
			}
		}
		return OBJECT;
	}

}
