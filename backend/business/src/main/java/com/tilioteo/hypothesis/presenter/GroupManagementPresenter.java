/**
 * 
 */
package com.tilioteo.hypothesis.presenter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.vaadin.dialogs.ConfirmDialog;

import com.tilioteo.hypothesis.data.CaseInsensitiveItemSorter;
import com.tilioteo.hypothesis.data.model.FieldConstants;
import com.tilioteo.hypothesis.data.model.Group;
import com.tilioteo.hypothesis.data.model.Pack;
import com.tilioteo.hypothesis.data.model.User;
import com.tilioteo.hypothesis.data.service.GroupService;
import com.tilioteo.hypothesis.data.service.PermissionService;
import com.tilioteo.hypothesis.data.service.RoleService;
import com.tilioteo.hypothesis.event.interfaces.MainUIEvent;
import com.tilioteo.hypothesis.server.Messages;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import net.engio.mbassy.listener.Handler;

/**
 * @author kamil
 *
 */
@SuppressWarnings("serial")
public class GroupManagementPresenter extends AbstractManagementPresenter {

	private PermissionService permissionService;
	private GroupService groupService;

	private GroupWindowPresenter groupWindowPresenter;

	public GroupManagementPresenter() {
		permissionService = PermissionService.newInstance();
		groupService = GroupService.newInstance();

		groupWindowPresenter = new GroupWindowPresenter(bus);
	}

	@Override
	public Component buildHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.setWidth("100%");
		header.setSpacing(true);

		Label title = new Label(Messages.getString("Caption.Label.GroupsManagement"));
		title.addStyleName(ValoTheme.LABEL_HUGE);
		header.addComponent(title);
		header.addComponent(buildTools());
		header.setExpandRatio(title, 1);

		return header;
	}

	@Override
	protected Button buildAddButton() {
		final Button addButton = new Button(Messages.getString("Caption.Button.Add"));
		addButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		addButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				groupWindowPresenter.showWindow();
			}
		});

		return addButton;
	}

	@Override
	protected ComboBox buildSelection() {
		final ComboBox selectionType = new ComboBox();
		selectionType.setTextInputAllowed(false);
		selectionType.setNullSelectionAllowed(false);

		selectionType.addItem(Messages.getString("Caption.Item.Selected"));
		selectionType.addItem(Messages.getString("Caption.Item.All"));
		selectionType.select(Messages.getString("Caption.Item.Selected"));

		selectionType.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				allSelected = selectionType.getValue().equals(Messages.getString("Caption.Item.All"));
				bus.post(new MainUIEvent.GroupSelectionChangedEvent());
			}
		});

		return selectionType;
	}

	@Override
	protected Button buildUpdateButton() {
		Button updateButton = new Button(Messages.getString("Caption.Button.Update"));
		updateButton.setClickShortcut(KeyCode.ENTER);
		updateButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				Collection<Group> groups = getSelectedGroups();

				if (groups.size() == 1) {
					groupWindowPresenter.showWindow(groups.iterator().next());
				} else {
					groupWindowPresenter.showWindow(groups);
				}
			}
		});
		return updateButton;
	}

	@Override
	protected Button buildDeleteButton() {
		Button deleteButton = new Button(Messages.getString("Caption.Button.Delete"));
		deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		deleteButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				String question = allSelected ? Messages.getString("Caption.Confirm.Group.DeleteAll")
						: Messages.getString("Caption.Confirm.Group.DeleteSelected");

				deletionConfirmDialog = ConfirmDialog.show(UI.getCurrent(),
						Messages.getString("Caption.Dialog.ConfirmDeletion"), question,
						Messages.getString("Caption.Button.Confirm"), Messages.getString("Caption.Button.Cancel"),
						GroupManagementPresenter.this);
			}
		});
		return deleteButton;
	}

	@Override
	protected Resource getExportResource() {
		StreamResource.StreamSource source = new StreamResource.StreamSource() {
			@Override
			public InputStream getStream() {
				return getExportFile();
			}
		};

		String filename = Messages.getString("Caption.Export.GroupFileName");
		StreamResource resource = new StreamResource(source, filename);

		return resource;
	}

	private InputStream getExportFile() {
		try {
			OutputStream output = new ByteArrayOutputStream();
			SXSSFWorkbook workbook = new SXSSFWorkbook(-1);

			Sheet sheet = workbook.createSheet(Messages.getString("Caption.Export.GroupSheetName"));

			int rowNr = 0;
			Row row = sheet.createRow(rowNr++);
			sheet.createFreezePane(0, 1);

			row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(Messages.getString("Caption.Field.Id"));
			row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(Messages.getString("Caption.Field.Name"));

			for (Iterator<Group> i = getSelectedGroups().iterator(); i.hasNext();) {
				row = sheet.createRow(rowNr++);
				Group group = i.next();
				row.createCell(0, Cell.CELL_TYPE_NUMERIC).setCellValue(group.getId());
				row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(group.getName());
			}
			workbook.write(output);
			workbook.close();
			output.close();

			return new ByteArrayInputStream(((ByteArrayOutputStream) output).toByteArray());

		} catch (IOException e) {
			Notification.show(Messages.getString("Message.Error.ExportCreateFile"), e.getMessage(), Type.ERROR_MESSAGE);
		}

		return null;
	}

	private void deleteGroups() {
		Collection<Group> groups = getSelectedGroups();

		for (Iterator<Group> iterator = groups.iterator(); iterator.hasNext();) {
			Group group = iterator.next();
			group = groupService.merge(group);
			Set<User> users = new HashSet<User>();
			for (User user : group.getUsers()) {
				users.add(user);
			}

			/*
			 * for (User user : users) { group.removeUser(user); }
			 */

			groupService.delete(group);

			for (User user : users) {
				if (user != null) {
					bus.post(new MainUIEvent.UserGroupsChangedEvent(user));
				}
			}

			table.removeItem(group.getId());
		}
	}

	@SuppressWarnings("unchecked")
	private Collection<Long> getSelectedGroupIds() {
		Collection<Long> userIds;
		if (allSelected) {
			userIds = (Collection<Long>) table.getItemIds();
		} else {
			userIds = (Collection<Long>) table.getValue();
		}
		return userIds;
	}

	@SuppressWarnings("unchecked")
	private Collection<Group> getSelectedGroups() {
		Collection<Group> groups = new HashSet<Group>();
		for (Long id : getSelectedGroupIds()) {
			groups.add(((BeanItem<Group>) table.getItem(id)).getBean());
		}
		return groups;
	}

	@Override
	public Table buildTable() {
		table = new Table();
		table.setSizeFull();
		table.addStyleName(ValoTheme.TABLE_SMALL);
		table.setSelectable(true);
		table.setMultiSelect(true);
		table.setColumnCollapsingAllowed(true);
		table.setSortContainerPropertyId(FieldConstants.NAME);

		BeanContainer<Long, Group> dataSource = new BeanContainer<Long, Group>(Group.class);
		dataSource.setBeanIdProperty(FieldConstants.ID);

		List<Group> groups;
		if (loggedUser.hasRole(RoleService.ROLE_SUPERUSER)) {
			groups = groupService.findAll();
		} else {
			groups = groupService.findOwnerGroups(loggedUser);
		}
		for (Group group : groups) {
			group = groupService.merge(group);
			dataSource.addBean(group);
		}
		table.setContainerDataSource(dataSource);
		dataSource.setItemSorter(new CaseInsensitiveItemSorter());
		table.sort();

		table.addGeneratedColumn(FieldConstants.USERS, this);
		table.addGeneratedColumn(FieldConstants.AVAILABLE_PACKS, this);

		table.setVisibleColumns(FieldConstants.ID, FieldConstants.NAME, FieldConstants.USERS,
				FieldConstants.AVAILABLE_PACKS, FieldConstants.NOTE);

		table.setColumnHeaders(Messages.getString("Caption.Field.Id"), Messages.getString("Caption.Field.Name"),
				Messages.getString("Caption.Field.Users"), Messages.getString("Caption.Field.AvailablePacks"),
				Messages.getString("Caption.Field.Note"));

		table.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(final ValueChangeEvent event) {
				bus.post(new MainUIEvent.GroupSelectionChangedEvent());
			}
		});

		table.addItemClickListener(new ItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					Group group = ((BeanItem<Group>) event.getItem()).getBean();
					groupWindowPresenter.showWindow(group);
				}
			}
		});

		return table;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		if (columnId.equals(FieldConstants.USERS)) {
			Group group = ((BeanItem<Group>) source.getItem(itemId)).getBean();
			group = groupService.merge(group);

			Set<User> users = group.getUsers();
			List<String> sortedUsers = new ArrayList<String>();
			for (User user : users) {
				sortedUsers.add(user.getUsername());
			}
			Collections.sort(sortedUsers);
			Label usersLabel = new Label();

			StringBuilder descriptionBuilder = new StringBuilder();
			StringBuilder labelBuilder = new StringBuilder();
			for (String user : sortedUsers) {
				if (descriptionBuilder.length() != 0) {
					descriptionBuilder.append("<br/>");
					labelBuilder.append(", ");
				}
				descriptionBuilder.append(user);
				labelBuilder.append(user);
			}
			usersLabel.setDescription(descriptionBuilder.toString());

			if (users.size() < 5) {
				usersLabel.setValue(labelBuilder.toString());
			} else {
				usersLabel.setValue(Messages.getString("Caption.Label.MultipleUsers", users.size()));
			}
			return usersLabel;
		}

		else if (columnId.equals(FieldConstants.AVAILABLE_PACKS)) {
			Group group = ((BeanItem<Group>) source.getItem(itemId)).getBean();

			Set<Pack> packs = permissionService.getGroupPacks(group);
			List<String> sortedPacks = new ArrayList<String>();
			List<String> sortedPackDescs = new ArrayList<String>();
			for (Pack pack : packs) {
				sortedPacks.add(Messages.getString("Caption.Item.PackLabel", pack.getName(), pack.getId()));
				sortedPackDescs.add(Messages.getString("Caption.Item.PackDescription", pack.getName(), pack.getId(),
						pack.getDescription()));
			}
			Collections.sort(sortedPacks);
			Collections.sort(sortedPackDescs);

			StringBuilder labelBuilder = new StringBuilder();
			for (String pack : sortedPacks) {
				if (labelBuilder.length() != 0) {
					labelBuilder.append(", ");
				}
				labelBuilder.append(pack);
			}
			StringBuilder descriptionBuilder = new StringBuilder();
			descriptionBuilder.append("<ul>");
			for (String pack : sortedPackDescs) {
				descriptionBuilder.append("<li>" + pack + "</li>");
			}
			descriptionBuilder.append("</ul>");

			Label packsLabel = new Label();
			packsLabel.setDescription(descriptionBuilder.toString());

			if (packs.size() < 5) {
				packsLabel.setValue(labelBuilder.toString());
			} else {
				packsLabel.setValue(Messages.getString("Caption.Label.MultiplePacks", packs.size()));
			}

			return packsLabel;
		}

		return null;
	}

	@Override
	public void onClose(ConfirmDialog dialog) {
		if (dialog.isConfirmed()) {
			if (dialog.equals(deletionConfirmDialog)) {
				try {
					deleteGroups();
					Notification.show(Messages.getString("Message.Info.GroupsDeleted"));

				} catch (Exception e) {
					Notification.show(Messages.getString("Message.Error.GroupsDeletion"), e.getMessage(),
							Notification.Type.ERROR_MESSAGE);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Handler
	public void addGroupIntoTable(final MainUIEvent.GroupAddedEvent event) {
		Group group = event.getGroup();
		if (group != null) {
			BeanContainer<Long, Group> container = (BeanContainer<Long, Group>) table.getContainerDataSource();

			container.removeItem(group.getId());
			container.addItem(group.getId(), group);

			table.sort();
		}
	}

	@SuppressWarnings("unchecked")
	@Handler
	public void changeGroupUsers(final MainUIEvent.GroupUsersChangedEvent event) {
		Group group = event.getGroup();
		BeanContainer<Long, Group> container = (BeanContainer<Long, Group>) table.getContainerDataSource();

		container.removeItem(group.getId());
		container.addItem(group.getId(), group);
	}

	@SuppressWarnings("unchecked")
	@Handler
	public void setToolsEnabled(final MainUIEvent.GroupSelectionChangedEvent event) {
		boolean itemsSelected = ((Set<Object>) table.getValue()).size() > 0;
		boolean toolsEnabled = allSelected || itemsSelected;
		buttonGroup.setEnabled(toolsEnabled);
	}

}