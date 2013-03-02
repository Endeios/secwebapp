	<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>SecureApp</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link rel="stylesheet" href="<c:url value="/css/bootstrap.css"/>"></link>
<link rel="stylesheet" href="<c:url value="/css/app.css"/>"></link>
<script src="<c:url value="/js/jquery-1.9.0.js"/>"></script>
<script src="<c:url value="/js/underscore.js"/>"></script>
<script src="<c:url value="/js/backbone.js"/>"></script>
<script src="<c:url value="/js/bootstrap.js"/>"></script>
</head>
<body>
	<!-- Part 1: Wrap all page content here -->
	<div id="wrap">

		<!-- Begin page content -->
		<div class="container">
			<div class="page-header">
				<h1>Secure App</h1>
			</div>
			<p class="lead">Clicca sulle zone per attivare e disattivare</p>
			<div class="zona" id="A">
				<div class="sensor"></div>
			</div>
			<div class="zona" id="B">
				<div class="sensor"></div>
			</div>
			<div class="panel" id="alarm">
				<div class="sendAlarm btn btn-error">Alarm</div>
				<div class="resetAlarm btn">Reset</div>
				<label for="modoLadro">Modalità Furtiva</label><input type="checkbox" name="modoLadro" class="modoLadro"/>
			</div>
			<div class="lista" id="lista">
				<table>
					<thead>
						<tr>
							<th>Time</th>
							<th>Zone</th>
							<th>Event</th>
						</tr>
					</thead>
					<tbody id="logBody"></tbody>
					<tfoot></tfoot>
				</table>
			</div>
		</div>
		<div id="push"></div>
	</div>
	<c:url value="/v1/API/entry" var="logEntries"></c:url>
	<script type="text/javascript">
		logEntries = "${logEntries}";
		$(document).ready(
				function() {
					ZonaView = Backbone.View.extend({
						initialize : function() {
							this.zone = this.$el[0].id;
							console.log("Init Zone " + this.zone);
							this.state = "inactive";
							this.refreshState();
							this.$el.append("<p>" + this.zone + "</p>");
							this.modoLadro = false;
						},
						setModoLadro:function(){
							this.modoLadro = true;
						},
						unsetModoLadro:function(){
							this.modoLadro = false;
						},
						events : {
							"click" : "handleClick",
							"random:event" : "handleAlarmEvent",
							"reset:alarm" : "resetAlarm",
							"mouseover":"checkLadro"
							
						},checkLadro:function(event){
							if(this.modoLadro)
								this.handleAlarmEvent(event);
						},
						handleAlarmEvent : function(event) {
							console.log("Alarm Handling...");
							switch (this.state) {
							case "active":
								this.state = "alarm";
								this.refreshState();
								console.log(this.zone
										+ " Is now in ALARM MODE ");
								$("#lista").trigger("alarm:event", this);
								break;
							default:
								break;
							}
							this.refreshState();
						},
						handleClick : function(event) {
							console.log(event);
							this.modifyState();
							this.refreshState();
						},
						declareState : function() {
							console.log("State is " + this.state);
						},
						modifyState : function() {
							switch (this.state) {
							case "inactive":
								this.state = "active";
								break;
							case "active":
								this.state = "inactive";
								break;
							case "alarm":
								this.state = "alarm";
								break;
							}
						},
						refreshState : function() {
							switch (this.state) {
							case "inactive":
								this.$el.addClass("active");
								this.$el.removeClass("inactive");
								this.$el.removeClass("alarm");
								break;
							case "active":
								this.$el.removeClass("alarm");
								this.$el.addClass("inactive");
								this.$el.removeClass("active");
								break;
							case "alarm":
								this.$el.addClass("alarm");
								this.$el.removeClass("active");
								break;
							default:
								this.declareState();
								break;
							}
						},
						resetAlarm : function(event) {
							console.log("Resetting zone " + this.zone);
							this.state = "inactive";
							this.refreshState();
						}
					});

					AlarmPanel = Backbone.View.extend({
						events : {
							"click div.sendAlarm" : "sendAlarm",
							"click div.resetAlarm" : "resetAlarm",
							"click .modoLadro":"check"
						},
						check:function(event){
							console.log(event.target);
							if(event.target.checked ==true){
								a.setModoLadro();
								b.setModoLadro();
							}else{
								a.unsetModoLadro();
								b.unsetModoLadro();
							}
						},
						sendAlarm : function(event) {
							console.log("sending Alarm...");
							$(".sensor").trigger("random:event");
						},
						resetAlarm : function(event) {
							console.log(event);
							console.log("Resetting all alarms");
							$(".zona").trigger("reset:alarm");
						}
					});

					LogEntry = Backbone.Model.extend({
						urlRoot : logEntries,
						idAttribute : "uuid",
						initialize : function() {
							this.set("date", new Date(this.get("time")));
							console.log("Init for entry " + this.id + " "
									+ this.get("date"));
						}
					});
					Log = Backbone.Collection.extend({
						model : LogEntry,
						url : logEntries,
						initialize : function() {
							this.fetch({
								success : function() {
									$("#lista").trigger("entry:Loaded");
								}
							});
						}
					});

					LogView = Backbone.View.extend({
						initialize : function() {
							console.log("Init log view");
							this.data = new Log();
						},
						events : {
							"entry:Loaded" : "redisplay",
							"alarm:event" : "addEvent",
							"reloadList" : "reloadList",
							"sync":"reloadList",
						},
						redisplay : function(event) {
							$("#logBody").empty();
							this.data.forEach(function(logEntry) {
								var data = "<tr><td>" + logEntry.get("date")
										+ "</td><td>" + logEntry.get("zone")
										+ "</td><td>" + logEntry.get("event")
										+ "</td></tr>";
								$("#logBody").append(data);
							});
						},
						addEvent : function(event, zone) {
							console.log(event);
							var that = this;
							console.log(zone);
							var newEvent = new LogEntry({
								zone : zone.$el[0].id + "",
								event : "Intrusione"
							});
							newEvent.save(null,{success:function(data){
								that.$el.trigger("reloadList");
							}});

						},
						reloadList : function(event) {
							this.data = new Log();
						}
					});

					a = new ZonaView({
						el : $("#A")
					});
					b = new ZonaView({
						el : $("#B")
					});
					p = new AlarmPanel({
						el : $("#alarm")
					});

					l = new LogView({
						el : $("#lista")
					});
				});
	</script>

	<div id="footer">
		<div class="container">
			<p class="muted credit">
				<a href="mailto:endeios@gmail.com">Bruno Veronesi</a>
			</p>
		</div>
	</div>
</body>
</html>