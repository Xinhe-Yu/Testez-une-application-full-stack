describe('Register spec', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('should display the register form', () => {
    cy.get('mat-card-title').should('contain', 'Register');
    cy.get('input[formControlName="firstName"]').should('exist');
    cy.get('input[formControlName="lastName"]').should('exist');
    cy.get('input[formControlName="email"]').should('exist');
    cy.get('input[formControlName="password"]').should('exist');
    cy.get('button[type="submit"]').should('exist').and('be.disabled');
  });

  it('should validate form fields', () => {
    const submitButton = () => cy.get('button[type="submit"]');
    submitButton().should('be.disabled');
    cy.get('input[formControlName="email"]').type('invalidemail');
    submitButton().should('be.disabled');
    cy.get('input[formControlName="firstName"]').type('Ab');
    submitButton().should('be.disabled');
    cy.get('input[formControlName="firstName"]').clear().type('John');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').clear().type('john@example.com');
    cy.get('input[formControlName="password"]').type('password123');
    submitButton().should('be.enabled');
  });

  it('should handle successful registration', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: {}
    }).as('registerRequest');

    cy.get('input[formControlName="firstName"]').type('John');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').type('john@example.com');
    cy.get('input[formControlName="password"]').type('password123');
    cy.get('button[type="submit"]').click();

    cy.wait('@registerRequest');
    cy.url().should('include', '/login');
  });

  it('should handle registration error', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: { message: 'Registration failed' }
    }).as('registerRequest');

    cy.get('input[formControlName="firstName"]').type('John');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').type('john@example.com');
    cy.get('input[formControlName="password"]').type('password123');
    cy.get('button[type="submit"]').click();

    cy.wait('@registerRequest');
    cy.get('span.error').should('be.visible').and('contain', 'An error occurred');
    cy.url().should('include', '/register');
  });
})
