<%@ page language="java" %>
<%@ page errorPage="/common/Error.jsp" %>
<%@ taglib uri="struts-html-el" prefix="html" %>
<%@ taglib uri="struts-tiles" prefix="tiles" %>
<%@ taglib uri="jstl-fmt" prefix="fmt" %>
<%@ taglib uri="jstl-c" prefix="c" %>
<%@ taglib uri="hq" prefix="hq" %>
<%--
  NOTE: This copyright does *not* cover user programs that use HQ
  program services by normal system calls through the application
  program interfaces provided as part of the Hyperic Plug-in Development
  Kit or the Hyperic Client Development Kit - this is merely considered
  normal use of the program, and does *not* fall under the heading of
  "derived work".
  
  Copyright (C) [2004, 2005, 2006], Hyperic, Inc.
  This file is part of HQ.
  
  HQ is free software; you can redistribute it and/or modify
  it under the terms version 2 of the GNU General Public License as
  published by the Free Software Foundation. This program is distributed
  in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
  even the implied warranty of MERCHANTABILITY or FITNESS FOR A
  PARTICULAR PURPOSE. See the GNU General Public License for more
  details.
  
  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
  USA.
 --%>


<!-- Content Block Title: Properties -->
<tiles:insert definition=".header.tab">
  <tiles:put name="tabKey" value="alert.config.props.PropertiesBox"/>
</tiles:insert>

<!-- Properties Content -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
  <tr>
    <td colspan="4" class="BlockContent"><span style="height: 1px;"></span></td>
  </tr>
  <tr valign="top">
    <td width="20%" class="BlockLabel"><fmt:message key="common.label.Name"/></td>
    <td width="30%" class="BlockContent"><c:out value="${alertDef.name}"/></td>
    <td width="20%" class="BlockLabel"><fmt:message key="alert.config.props.PB.Priority"/></td>
    <td width="30%" class="BlockContent">
      <fmt:message key="${'alert.config.props.PB.Priority.'}${alertDef.priority}"/>
    </td>
  </tr>
  <tr valign="top">
    <td width="20%" class="BlockLabel"><fmt:message key="common.label.Description"/></td>
    <td width="30%" class="BlockContent" rowspan="3"><c:out value="${alertDef.description}"/></td>
    <td width="20%" class="BlockLabel"><fmt:message key="alert.config.props.PB.Active"/></td>
    <tiles:insert page="/resource/common/monitor/alerts/config/AlertDefinitionActive.jsp">
    <tiles:put name="alertDef" beanName="alertDef"/>
    </tiles:insert>
  </tr>
  <tr valign="top">
    <td width="20%" class="BlockLabel">&nbsp;</td>
    <td width="20%" class="BlockLabel"><fmt:message key="alert.config.props.PB.DateCreated"/></td>
    <td width="30%" class="BlockContent"><hq:dateFormatter time="false" value="${alertDef.ctime}"/></td>
  </tr>
  <tr valign="top">
    <td width="20%" class="BlockLabel">&nbsp;</td>
    <td width="20%" class="BlockLabel"><fmt:message key="alert.config.props.PB.DateMod"/></td>
    <td width="30%" class="BlockContent"><hq:dateFormatter time="false" value="${alertDef.mtime}"/></td>
  </tr>
  <c:if test="${alertDef.parentId > 0}">
  <tr>
    <td colspan="4" class="BlockContent"><span style="height: 3px;"></span></td>
  </tr>
  <tr>
    <td colspan="4" class="BlockContent"><span class="red" style="padding-left: 15px;"><fmt:message key="alerts.config.service.DefinitionList.isResourceAlert.false"/></span> <fmt:message key="alert.config.props.PB.IsTypeAlert"/>
</td>
  </tr>
  </c:if>
  <tr>
    <td colspan="4" class="BlockContent"><span style="height: 1px;"></span></td>
  </tr>
  <tr>
    <td colspan="4" class="BlockBottomLine"><span style="height: 1px;"></span></td>
  </tr>
</table>
<tiles:insert definition=".toolbar.edit">
<c:choose>
  <c:when test="${not empty Resource}">
    <tiles:put name="editUrl">/alerts/Config.do?mode=editProperties&type=<c:out value="${Resource.entityId.type}"/>&rid=<c:out value="${Resource.id}"/>&ad=<c:out value="${alertDef.id}"/></tiles:put>
  </c:when>
  <c:otherwise>
    <tiles:put name="editUrl">/alerts/Config.do?mode=editProperties&aetid=<c:out value="${ResourceType.appdefTypeKey}"/>&ad=<c:out value="${alertDef.id}"/></tiles:put>
  </c:otherwise>
</c:choose>
</tiles:insert>
<br>
