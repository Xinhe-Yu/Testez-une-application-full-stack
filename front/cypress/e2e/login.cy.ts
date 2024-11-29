describe('Login spec', () => {
  beforeEach(() => {
    cy.visit('/login');
  });

  it('should display login form', () => {
    cy.get('mat-card-title').should('exist');
    cy.get('input[formControlName=email]').should('exist');
    cy.get('input[formControlName=password]').should('exist');
    cy.get('button[type=submit]').should('exist').and('be.disabled');
  });

  it('should successfully login and redirect to sessions page', () => {
    const userEmail = "yoga@studio.com";
    const userPassword = "test!1234";
    const userSession = "userSession.json";
    cy.login(userEmail, userPassword, userSession);

    cy.url().should('include', '/sessions');
  });

  it('should display error message on login failure', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { message: 'Invalid credentials' }
    }).as('loginRequest');

    cy.get('input[formControlName=email]').type("wrong@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.wait('@loginRequest');
    cy.get('p.error').should('contain', 'An error occurred');
    cy.url().should('include', '/login');
  })
});
