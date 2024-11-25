describe('Detail Component', () => {
  const userEmail = "yoga@studio.com";
  const userPassword = "test!1234";
  const adminSession = "adminSession.json";
  const yogaSession = "yogaSession.json";

  it('should create a new session successfully', () => {
    cy.login(userEmail, userPassword, adminSession);
    cy.create();

    cy.intercept('POST', '/api/session', {
      statusCode: 201,
      fixture: yogaSession
    }).as('createSession');

    // Fill out the form
    cy.get('input[formControlName="name"]').type('Yoga Basics');
    cy.get('input[formControlName="date"]').type('2024-11-20');

    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.wait('@getTeachers');
    cy.get('mat-option').first().click(); // Select the first teacher
    cy.get('textarea[formControlName="description"]').type('An introductory session to yoga.');

    // Submit the form
    cy.get('button[type="submit"]').click();

    // Wait for the create request and verify it was successful
    cy.wait('@createSession');

    // Check for success message
    cy.get('.mat-snack-bar-container').should('contain', 'Session created !');

    // Verify redirection to sessions list
    cy.url().should('include', '/sessions');
  });


  it('should validate required fields', () => {
    cy.login(userEmail, userPassword, adminSession);
    cy.create();

    // Attempt to submit without filling out required fields
    cy.get('button[type="submit"]').should('be.disabled');
  });
});
