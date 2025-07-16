describe('Card Image Component exists', () => {
  beforeEach(() => {
    cy.visit('/content/wknd/us/BlainesHomePage.html', {
      auth: {
        username: 'admin',
        password: 'admin'
      }
    });
  });

  it('should render the component container', () => {
    cy.get('[data-cmp-is="card-image"]')
      .should('exist')
      .and('be.visible');
  });


});

describe('Card Image Component Does Not Exist', () => {
  beforeEach(() => {
    cy.visit('/content/wknd/us/custompage.html', {
      auth: {
        username: 'admin',
        password: 'admin'
      }
    });
  });
  it('should not render the image', () => {
     cy.get('[data-cmp-is="card-image_image"]')
       .should('not.exist');
  });

  it('should not render the headline', () => {
     cy.get('[data-cmp-is="card-image_headline"]')
       .should('not.exist');
  });

  it('should not render the body text', () => {
     cy.get('[data-cmp-is="card-image_text"]')
       .should('not.exist');
  });

});