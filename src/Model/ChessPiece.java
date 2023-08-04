package Model;

public class ChessPiece {

	Spot position;
	Category category;
	Color color;
	String image;

	public ChessPiece(Category category, Color color, String image) {
		this.position = position;
		this.category = category;
		this.color = color;
		this.image = image;
	}

	public enum Category {
		PAWN,
		KING,
		QUEEN,
		ROOK,
		KNIGHT,
		BISHOP
	}

	public enum Color {
		BLACK,
		WHITE
	}
	public Spot getPosition() {
		return position;
	}

	public void setPosition(Spot position) {
		this.position = position;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
