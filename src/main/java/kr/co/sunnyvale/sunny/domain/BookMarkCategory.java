package kr.co.sunnyvale.sunny.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "BOOK_MARK_CATEGORY")
public class BookMarkCategory {
	
	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "bookMarkCategory", fetch = FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<BookMark> bookMarks;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private BookMarkCategory parent;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="parent")
	private List<BookMarkCategory> children;
	
	@Column(name = "thread")
	private long thread;
	
	@Column(name = "depth")
	private int depth;
	
	@Column(name = "absolute_path")
	private String absolutePath;
	
	@Column(name = "ordering")
	private int ordering;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<BookMark> getBookMarks() {
		return bookMarks;
	}

	public void setBookMarks(List<BookMark> bookMarks) {
		this.bookMarks = bookMarks;
	}

	public BookMarkCategory getParent() {
		return parent;
	}

	public void setParent(BookMarkCategory parent) {
		this.parent = parent;
	}

	public List<BookMarkCategory> getChildren() {
		return children;
	}

	public void setChildren(List<BookMarkCategory> children) {
		this.children = children;
	}

	public long getThread() {
		return thread;
	}

	public void setThread(long thread) {
		this.thread = thread;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	} 
	
	
	
}
