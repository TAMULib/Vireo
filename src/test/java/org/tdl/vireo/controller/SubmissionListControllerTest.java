package org.tdl.vireo.controller;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.tdl.vireo.model.NamedSearchFilter;
import org.tdl.vireo.model.NamedSearchFilterGroup;
import org.tdl.vireo.model.SubmissionListColumn;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.tamu.weaver.response.ApiStatus;
@SuppressWarnings("unchecked")
public class SubmissionListControllerTest  extends AbstractControllerTest {

	@Test
	public void testGetSubmissionViewColumns() {
		response = submissionListController.getSubmissionViewColumns();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<SubmissionListColumn>  submissionListColumnList = (List<SubmissionListColumn>) response.getPayload().get("ArrayList<SubmissionListColumn>");
		assertEquals(" There are no submissionListColumns in thelist " , mockSubmissionListColumnList.size() , submissionListColumnList.size());
	}

	@Test
	public void testGetSubmissionViewColumnsByUser()  {
		TEST_USER.getActiveFilter().setColumnsFlag(true);
		TEST_USER.getActiveFilter().setSavedColumns(mockSubmissionListColumnList);
		response = submissionListController.getSubmissionViewColumnsByUser(TEST_USER);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<SubmissionListColumn> submissionListColumnList = (List<SubmissionListColumn>) response.getPayload().get("ArrayList<SubmissionListColumn>");
		assertEquals(" There are no user submission view columns in thelist " , mockSubmissionListColumnList.size() , submissionListColumnList.size());
	}

	@Test
	public void testgetFilterColumnsByUser() throws Exception {
		List<SubmissionListColumn> mockSubmissionListColumnList = new ArrayList<>(Arrays.asList(new SubmissionListColumn[] {TEST_SUBMISSION_LIST_COL1, TEST_SUBMISSION_LIST_COL2 }));
		TEST_USER.setFilterColumns(mockSubmissionListColumnList);
		response  = submissionListController.getFilterColumnsByUser(TEST_USER);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<SubmissionListColumn> submissionListColumnList = (List<SubmissionListColumn>) response.getPayload().get("ArrayList<SubmissionListColumn>");
		assertEquals(" The user has no filter columns in thelist " , mockSubmissionListColumnList.size() , submissionListColumnList.size());
	}

	@Test
	public void testGetSubmissionViewPageSizeByUser() {
		response = submissionListController.getSubmissionViewPageSizeByUser(TEST_USER);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Integer responsePageSize  = (Integer) response.getPayload().get("Integer");
		assertEquals(" The page size is incorrect ", new Integer(10) , responsePageSize);
	}

	@Test
	public void testUpdateUserSubmissionViewColumns() throws Exception {
		List<SubmissionListColumn> mockSubmissionListColumnList = new ArrayList<>(Arrays.asList(new SubmissionListColumn[] {TEST_SUBMISSION_LIST_COL1, TEST_SUBMISSION_LIST_COL2 }));
		activeFilter.setUser(TEST_USER);
		activeFilter.setColumnsFlag(true);
		TEST_USER.setActiveFilter(activeFilter);
		TEST_USER.setSubmissionViewColumns(mockSubmissionListColumnList);
		response = submissionListController.updateUserSubmissionViewColumns(TEST_USER, new Integer(10), TEST_USER.getSubmissionViewColumns());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<SubmissionListColumn> submissionListColumnList = (List<SubmissionListColumn>) response.getPayload().get("ArrayList<SubmissionListColumn>");
		assertEquals(" The submission list column is empty  " , mockSubmissionListColumnList.size() , submissionListColumnList.size());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testResetUserSubmissionViewColumns() {
		activeFilter.setColumnsFlag(true);
		TEST_USER.setActiveFilter(activeFilter);
		response = submissionListController.resetUserSubmissionViewColumns(TEST_USER);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List submissionListColumnList = (List) response.getPayload().get("ArrayList");
		assertEquals(" The user submission columns are not reset ", 0 , submissionListColumnList.size());
	}

	@Test
	public void testUpdateUserFilterColumns() throws Exception {
		List<SubmissionListColumn> mockSubmissionListColumnList = new ArrayList<>(Arrays.asList(new SubmissionListColumn[] {TEST_SUBMISSION_LIST_COL1, TEST_SUBMISSION_LIST_COL2 }));
		TEST_USER.setFilterColumns(mockSubmissionListColumnList);
		response = submissionListController.updateUserFilterColumns(TEST_USER, TEST_USER.getFilterColumns());
		List<SubmissionListColumn> updatedUserFilterColumnsList = (List<SubmissionListColumn>) response.getPayload().get("ArrayList<SubmissionListColumn>");
		assertEquals("There are no user filter columns ",TEST_USER.getFilterColumns().size() , updatedUserFilterColumnsList.size());
	}

	@Test
	public void testSetActiveFilter() {
		activeFilter.setId(1l);
		TEST_USER.setActiveFilter(activeFilter);
		response = submissionListController.setActiveFilter(TEST_USER, TEST_USER.getActiveFilter());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testGetActiveFilters() throws Exception {
		TEST_USER.setActiveFilter(activeFilter);
		response = submissionListController.getActiveFilters(TEST_USER);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		NamedSearchFilterGroup responseActiveFilter = (NamedSearchFilterGroup) response.getPayload().get("NamedSearchFilterGroup");
		assertEquals(" The response has the wrong filter ", TEST_USER.getActiveFilter().getName(), responseActiveFilter.getName());
	}

	@Test
	public void testGetSavedFilters() throws Exception {
		TEST_USER.setActiveFilter(activeFilter);
		response = submissionListController.getSavedFilters(TEST_USER);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		NamedSearchFilterGroup responseActiveFilter = (NamedSearchFilterGroup) response.getPayload().get("NamedSearchFilterGroup");
		assertEquals(" The response has the wrong saved filter ", TEST_USER.getActiveFilter().getName(), responseActiveFilter.getName());
	}

	@Test
	public void testRemoveSavedFilter() {
		activeFilter.setUser(TEST_USER);
		TEST_USER.setActiveFilter(activeFilter);
		response = submissionListController.removeSavedFilter(TEST_USER, TEST_USER.getActiveFilter());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		NamedSearchFilterGroup responseNamedSearchFilterGroup = (NamedSearchFilterGroup) response.getPayload().get("NamedSearchFilterGroup");
		assertEquals(" The saved filter was not removed ", 0, responseNamedSearchFilterGroup.getUser().getSavedFilters().size() );
		assertEquals(" The response has the returned incorrect filter ", TEST_USER.getActiveFilter().getName(), responseNamedSearchFilterGroup.getUser().getActiveFilter().getName());
	}

	@Test
	public void testAddFilterCriterion() throws Exception  {
		Set<NamedSearchFilter> mockNamedSearchFilterSet = new HashSet<>(Arrays.asList(new NamedSearchFilter[] { TEST_NAME_SREARCH_FILTER1, TEST_NAME_SREARCH_FILTER2 }));
		NamedSearchFilterGroup activeFilter = new NamedSearchFilterGroup();
		activeFilter.setName("activeFilter Name");
		activeFilter.setNamedSearchFilters(mockNamedSearchFilterSet);
		TEST_USER.setActiveFilter(activeFilter);
		TEST_NAME_SREARCH_FILTER1.setName("criterionName");

		Map<String, Object> data  = new HashMap<String, Object>();
		data.put("criterionName", "criterionName");
        data.put("filterValue", "Filter Criterion field value");
        data.put("exactMatch", true);
        data.put("filterGloss", "Filter Criterion field gloss");

        response = submissionListController.addFilterCriterion(TEST_USER, data);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testRemoveFilterCriterion() {
		TEST_NAME_SREARCH_FILTER1.setName("criterionName");
		activeFilter.setNamedSearchFilters(mockNamedSearchFilterSet);
		TEST_USER.setActiveFilter(activeFilter);
		TEST_USER.getActiveFilter().addFilterCriterion(TEST_NAME_SREARCH_FILTER1);
		response =submissionListController.removeFilterCriterion(TEST_USER, TEST_USER.getActiveFilter().getNamedSearchFilter(TEST_NAME_SREARCH_FILTER1.getId()).getName(),
				TEST_FILTER_CRITERION1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testClearFilterCriteria() {
		List<SubmissionListColumn> mockSubmissionListColumnList = new ArrayList<>(Arrays.asList(new SubmissionListColumn[] {TEST_SUBMISSION_LIST_COL1, TEST_SUBMISSION_LIST_COL2 }));

		TEST_USER.setActiveFilter(activeFilter);
		TEST_USER.getActiveFilter().setNamedSearchFilters(mockNamedSearchFilterSet);
		TEST_USER.getActiveFilter().setSavedColumns(mockSubmissionListColumnList);
		TEST_USER.getActiveFilter().setColumnsFlag(true);
		response = submissionListController.clearFilterCriteria(TEST_USER);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testGetAllSaveFilterCriteria()  {
		TEST_USER.setActiveFilter(activeFilter);
		TEST_USER.setSavedFilters(mockNamedSearchFilterGroupList);
		response = submissionListController.getAllSaveFilterCriteria(TEST_USER);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<NamedSearchFilterGroup> userSavedFilters = (List<NamedSearchFilterGroup>) response.getPayload().get("ArrayList<NamedSearchFilterGroup>");
		assertEquals(" The user does not have saved filters ", mockNamedSearchFilterGroupList.size() , userSavedFilters.size() );
	}

	@Test
	public void testSaveFilterCriteria() {
		NamedSearchFilterGroup activeFilter = new NamedSearchFilterGroup();
		activeFilter.setName("activeFilterName");
		List<NamedSearchFilterGroup> mockNamedSearchFilterGroupList = new ArrayList<>(Arrays.asList(new NamedSearchFilterGroup[] { activeFilter, activeFilter }));
		TEST_USER.setSavedFilters(mockNamedSearchFilterGroupList);
		TEST_USER.setActiveFilter(activeFilter);
		response = submissionListController.saveFilterCriteria(TEST_USER, activeFilter);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}
}
