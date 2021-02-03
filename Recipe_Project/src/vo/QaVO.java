package vo;

import java.util.Date;

public class QaVO {
	private int qa_idx;
	private String qa_title;
	private String qa_id;
	private String qa_content;
	private int qa_depth;
	private int qa_mainboard;
	private Date qa_date;
	private String qa_state;
	private String qa_answer_state;
	private int qa_view;
	
	
	public String getQa_answer_state() {
		return qa_answer_state;
	}
	public void setQa_answer_state(String qa_answer_state) {
		this.qa_answer_state = qa_answer_state;
	}
	public int getQa_idx() {
		return qa_idx;
	}
	public void setQa_idx(int qa_idx) {
		this.qa_idx = qa_idx;
	}
	public String getQa_title() {
		return qa_title;
	}
	public void setQa_title(String qa_title) {
		this.qa_title = qa_title;
	}
	public String getQa_id() {
		return qa_id;
	}
	public void setQa_id(String qa_id) {
		this.qa_id = qa_id;
	}
	public String getQa_content() {
		return qa_content;
	}
	public void setQa_content(String qa_content) {
		this.qa_content = qa_content;
	}
	public int getQa_depth() {
		return qa_depth;
	}
	public void setQa_depth(int qa_depth) {
		this.qa_depth = qa_depth;
	}
	public int getQa_mainboard() {
		return qa_mainboard;
	}
	public void setQa_mainboard(int qa_mainboard) {
		this.qa_mainboard = qa_mainboard;
	}
	public Date getQa_date() {
		return qa_date;
	}
	public void setQa_date(Date qa_date) {
		this.qa_date = qa_date;
	}
	public String getQa_state() {
		return qa_state;
	}
	public void setQa_state(String qa_state) {
		this.qa_state = qa_state;
	}
	public int getQa_view() {
		return qa_view;
	}
	public void setQa_view(int qa_view) {
		this.qa_view = qa_view;
	}
	
	
}
