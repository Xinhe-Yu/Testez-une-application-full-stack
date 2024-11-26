describe('App Navigation Buttons', () => {
  const userEmail = "yoga@studio.com";
  const userPassword = "test!1234";
  const adminSession = "adminSession.json";
  const userSession = "userSession.json";

  it('should show correctly info for user and permit user to delete their count', () => {
    cy.login(userEmail, userPassword, userSession);

    cy.intercept('GET', "/api/user/4", { fixture: 'userInfo.json' }).as('getUserInfo');
    cy.intercept('DELETE', '/api/user/4', { statusCode: 200 }).as('deleteUser');

    cy.get('span').contains('Account').click();

    cy.get('p').contains('You are admin').should('not.exist');
    cy.get('button').contains('delete').should('exist').click();
    cy.get('.mat-snack-bar-container').should('contain', 'Your account has been deleted !');
    const baseUrl = Cypress.config().baseUrl.endsWith('/') ? Cypress.config().baseUrl : Cypress.config().baseUrl + '/';
    cy.url().should('eq', baseUrl);
    cy.get('span').contains('Login').should('be.visible');

  });

  it('should navigate to create session page when Create button is clicked', () => {
    cy.visit('/login');
    cy.get('span').contains('Register').click();
    cy.url().should('include', '/register');
  });

  it('should log out and redirect to login page when Logout button is clicked', () => {
    cy.login(userEmail, userPassword, adminSession);

    cy.get('span').contains('Logout').click();
  });

  it('should navigate to sessions list page when Sessions button is clicked', () => {
    cy.login(userEmail, userPassword, adminSession);

    cy.get('span').contains('Sessions').click();
    cy.url().should('include', '/sessions');
  });

  it('should show correctly info for admin', () => {
    cy.login(userEmail, userPassword, adminSession);

    cy.intercept('GET', "/api/user/1", { fixture: 'adminInfo.json' }).as('getUserInfo');

    cy.get('span').contains('Account').click();
    cy.get('h1').should('contain', 'User information');
    cy.get('p').contains('Name:').should('contain', 'firstName LASTNAME');
    cy.get('p').contains('Email:').should('contain', 'admin@test.fr');
    cy.get('p').contains('You are admin').should('exist');
    cy.get('button').contains('delete').should('not.exist');

    cy.get('button[mat-icon-button]').click();
    cy.url().should('not.include', '/user/1');
  });
});
