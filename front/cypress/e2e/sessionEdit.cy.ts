import { Session } from '../../src/app/features/sessions/interfaces/session.interface';

describe('Detail Component', () => {
  const userEmail = "yoga@studio.com";
  const userPassword = "test!1234";
  const adminSession = "adminSession.json";
  const yogaSession = "yogaSession.json";

  it('should update an existing session successfully', () => {
    cy.login(userEmail, userPassword, adminSession);
    cy.edit();

    cy.fixture<Session>('yogaSessionUpdate.json').then((yogaSession) => {
      cy.intercept('PUT', `/api/session/1`, {
        statusCode: 200,
        body: yogaSession
      }).as('updateSession');
    });

    cy.get('input[formControlName="name"]').clear().type('Yoga Basics Updated');
    cy.get('input[formControlName="date"]').clear().type('2024-11-21');
    cy.get('textarea[formControlName="description"]').clear().type('Updated description.');
    cy.get('button[type="submit"]').click();

    // Wait for the update request and verify it was successful
    cy.wait('@updateSession');

    // Check for success message
    cy.get('.mat-snack-bar-container').should('contain', 'Session updated !');

    // Verify redirection to sessions list
    cy.url().should('include', '/sessions');
  });

  it('should create a new session successfully', () => {
    cy.login(userEmail, userPassword, adminSession);
    cy.create();

    cy.intercept('POST', '/api/session', {
      statusCode: 201,
      fixture: yogaSession
    }).as('createSession');

    cy.get('input[formControlName="name"]').type('Yoga Basics');
    cy.get('input[formControlName="date"]').type('2024-11-20');

    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').first().click(); // Select the first teacher
    cy.get('textarea[formControlName="description"]').type('An introductory session to yoga.');

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
