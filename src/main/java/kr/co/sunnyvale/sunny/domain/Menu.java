package kr.co.sunnyvale.sunny.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "MENU")
public class Menu {

	
	public static final int TYPE_NORMAL=0;
	public static final int TYPE_DEPARTMENT=1;
	public static final int TYPE_GROUP=2;
	public static final int TYPE_PROJECT=3;
	
	public static final String NAME_LOBBY="lobby";
	public static final String NAME_CHAT="chat";
	public static final String NAME_BOOKMARK="bookmark";
	public static final String NAME_CONTACT="contact";
	public static final String NAME_DEPARTMENT="department";
	public static final String NAME_GROUP="group";
	public static final String NAME_MEDIA = "media";
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "absolute_name")
	private String absoluteName;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description" )
	private String description;
	
	@Column(name = "relative_href")
	private String relativeHref;
	
	@Column(name = "icon_html")
	private String iconHtml;
	
	@Column(name = "extra_html")
	private String extraHtml;
	

	@Column(name = "ordering", columnDefinition="integer default 0" )
	private int ordering;

	@Column(name = "type", columnDefinition="integer default 0" )
	private int type;

	
	@OneToMany( mappedBy = "menu", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE} )
	private List<SiteMenu> siteMenus;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getRelativeHref() {
		return relativeHref;
	}

	public void setRelativeHref(String relativeHref) {
		this.relativeHref = relativeHref;
	}

	public String getIconHtml() {
		return iconHtml;
	}

	public void setIconHtml(String iconHtml) {
		this.iconHtml = iconHtml;
	}

	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}

	
	public List<SiteMenu> getSiteMenus() {
		return siteMenus;
	}

	public void setSiteMenus(List<SiteMenu> siteMenus) {
		this.siteMenus = siteMenus;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAbsoluteName() {
		return absoluteName;
	}

	public void setAbsoluteName(String absoluteName) {
		this.absoluteName = absoluteName;
	}

	public String getExtraHtml() {
		return extraHtml;
	}

	public void setExtraHtml(String extraHtml) {
		this.extraHtml = extraHtml;
	}
	
}
