# Work order Scheduler

**Work order scheduling is an application in Java created to daily schedule assignment for technicians for incoming stream of work orders.**

###### Functional Requirements

1. The application will match the skills on the work order with the skills on the technician
2. Check the duration of the work order and available technician working hours for the day and assign the work order to technician. 
3.It should also minimize the drive time of the technician.
4. Technician details will be stored in a database
5. Technician fields are ID, Name, Skill, Work Start Time, Work End Time (add more fields as required)
6. Technician records are assumed to be pre-populated by a separate application and ready for use. So these can be directly created in the database.
7. A web service needs to be exposed by the application to take in the incoming work orders
8. A web interface should be developed to create work orders details and invoke the above web service asynchronously
9. The work order fields are ID, Skill, Priority, Duration (add more fields as required)
10. The application should match the skills on the work order with the skills on the technician, check the duration of the work order and available technician working hours for the day and assign the work order to technician
11. The assignment should have driving time optimized i.e. during the route the maximum time of the technician should be utilized without requiring to drive again and again to the location. While honoring this requirement, Requirement 7 should not be violated.
12. The assignment of technician to work order has to be displayed in a web page.

###### Documenation Reference
1. Under documentation folder find class diagrams, sequence diagrams and DB model.
2. Documentation writeup is available for detailed implementation approach, technology stack used and sample input and outcome screenshots.
