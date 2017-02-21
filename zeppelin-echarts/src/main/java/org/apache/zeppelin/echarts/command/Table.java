package org.apache.zeppelin.echarts.command;

import org.apache.commons.lang3.ArrayUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ethan Xiao on 2017/2/18.
 */
public class Table {
	private String[] columns = new String[]{};
	private List<String[]> rows = new LinkedList<>();

	public void addRow(String[] row) {
		if (ArrayUtils.isEmpty(columns)) {
			throw new RuntimeException("columns is empty");
		}
		if (ArrayUtils.isEmpty(row)) {
			throw new RuntimeException("values is empty");
		}
		if (row.length != columns.length) {
			throw new RuntimeException("columns count " + columns.length + " not equals values count " + row.length);
		}
		if (rows == null) {
			rows = new LinkedList<>();
		}
		rows.add(row);
	}

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	public List<String[]> getRows() {
		return rows;
	}

}
