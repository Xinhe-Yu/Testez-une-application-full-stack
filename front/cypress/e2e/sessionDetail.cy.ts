describe('Detail Component', () => {
  const userEmail = "yoga@studio.com";
  const userPassword = "test!1234";
  const adminSession = "adminSession.json";
  const userSession = "userSession.json";
  const yogaSession = "yogaSession.json";

  it('should allow non-admin users to participate in a session', () => {
    cy.login(userEmail, userPassword, userSession);
    cy.detail(yogaSession);

    cy.intercept('POST', `/api/session/1/participate/4`, {
      statusCode: 200,
      body: {}
    }).as('participate');

    cy.get('button').contains('Participate').click();
    cy.wait('@participate');
  });

  it('should display session details correctly', () => {
    cy.login(userEmail, userPassword, adminSession);
    cy.detail(yogaSession);

    cy.get('h1').should('contain', 'Yoga');
    cy.get('.description').should('contain', 'Description:');
    cy.get('.ml1').contains('attendees');
  });

  it('should display session details correctly for admin user', () => {
    cy.login(userEmail, userPassword, adminSession);
    cy.detail(yogaSession);

    cy.get('h1').should('contain', 'Yoga Basics');
    cy.get('button').contains('Delete').should('exist'); // Admin-only button
  });

  it('should allow admin to delete a session', () => {
    cy.login(userEmail, userPassword, adminSession);
    cy.detail(yogaSession);

    cy.intercept('DELETE', '/api/session/1', {
      statusCode: 200,
      body: {}
    }).as('deleteSession');

    cy.get('button').contains('Delete').click(); // Click delete button
    cy.wait('@deleteSession'); // Wait for delete request

    // Verify navigation after deletion
    cy.url().should('include', '/sessions'); // Ensure redirected to sessions list
    cy.get('.items').should('not.contain', 'Yoga Basics'); // Ensure deleted session is not present
  });
});
