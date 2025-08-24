describe('Custom Accordion Component with custom headline', () => {
  beforeEach(() => {
    cy.visit('/content/wknd/us/custompage.html', {
      auth: {
        username: 'admin',
        password: 'admin'
      }
    });
  });

  it('should render the Custom Accordion with a custom headline', () => {
    cy.get('.cmp-accordion')
      .should('exist')
      .and('be.visible');

    cy.get('.cmp-accordion .cmp-accordion__headline')
      .should('contain.text', 'Custom Accordion');
  });
});

describe('Custom Accordion Component without a custom headline', () => {
  beforeEach(() => {
    cy.visit('/content/wknd/us/en.html', {
      auth: {
        username: 'admin',
        password: 'admin'
      }
    });
  });

  it('should render the Custom Accordion without a custom headline', () => {
    cy.get('.cmp-accordion')
      .should('exist')
      .and('be.visible');

    cy.get('.cmp-accordion .cmp-accordion__headline')
      .should('not.exist');
  });
});