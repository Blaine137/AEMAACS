describe('Featured Headline Component exists', () => {
  beforeEach(() => {
    cy.visit('/content/wknd/us/custompage.html', {
      auth: {
        username: 'admin',
        password: 'admin'
      }
    });
  });

  it('should render the component container', () => {

    cy.get('[class="cmp-title"]')
      .should('exist')
      .and('be.visible');
  });

  it('should render the primary title', () => {
    cy.get('[class="cmp-title__text"]')
      .should('exist')
      .and('be.visible');
  });

  it('should render the secondary title', () => {
    cy.get('[class="cmp-title__subheadline"]')
      .should('exist')
      .and('be.visible');
  });

  it('should find and click the link', () => {
     cy.visit('/content/wknd/us/Featured-Properties.html', {
        auth: {
          username: 'admin',
          password: 'admin'
        }
     });

     cy.get('[class="cmp-title__link"]')
       .click();
  });


});