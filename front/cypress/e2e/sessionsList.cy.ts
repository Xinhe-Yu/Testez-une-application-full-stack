describe('Sessions Module', () => {
  const userEmail = "yoga@studio.com";
  const userPassword = "test!1234";
  const userSession = "userSession.json";
  const adminSession = "adminSession.json";

  it('should navigate to session detail page when Detail button is clicked', () => {
    cy.login(userEmail, userPassword, userSession);

    cy.get('button').contains('Detail').first().click();
    cy.url().should('include', '/detail/1'); // Adjust based on your routing
  });

  it('should navigate to edit session page when Edit button is clicked', () => {
    cy.login(userEmail, userPassword, adminSession);

    cy.scrollTo('bottom');
    cy.get('button').contains('Edit').first().click();
    cy.url().should('include', '/update/1'); // Adjust based on your routing
  });

  it('should display the list of sessions', () => {
    cy.login(userEmail, userPassword, adminSession);

    cy.get('mat-card-title').first().should('contain', 'Rentals available');
    cy.get('mat-card.item').first().within(() => {
      cy.get('mat-card-title').should('contain', 'Yoga Basics');
      cy.get('mat-card-subtitle').should('contain', 'Session on November 20, 2024');
      cy.get('img.picture').should('have.attr', 'src', 'assets/sessions.png');
      cy.get('mat-card-content p').should('contain', 'introductory session to yoga');
      cy.get('button').contains('Detail').should('exist');
    });
  });

  it('should show create button for admin users', () => {
    cy.login(userEmail, userPassword, adminSession);

    cy.get('button').contains('Create').should('exist');
  });

  it('should not show create or edit button for non-admin users', () => {
    cy.login(userEmail, userPassword, userSession);

    cy.get('mat-card.item').should('have.length', 1);
    cy.get('button').contains('Create').should('not.exist');
    cy.get('button').contains('edit').should('not.exist');
  });
});
