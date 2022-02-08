package model;

public interface Serialization {

	String serialize(HasiaciPristroj pristroj);
	HasiaciPristroj deserialize(String str);
}
