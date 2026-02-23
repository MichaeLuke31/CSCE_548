import java.time.Instant;
import java.util.Objects;

public class MealPlan {
	private Integer id;
	private Integer userId;
	private String name;
	private String description;
	private Instant createdAt;

	public MealPlan() {
	}

	// getters & setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof MealPlan))
			return false;
		MealPlan mp = (MealPlan) o;
		return Objects.equals(id, mp.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "MealPlan{" + "id=" + id + ", name='" + name + '\'' + '}';
	}
}
