describe('product update test', () => {
    it('visits the root', () => {
    cy.visit('/');
    });
    it('clicks the menu button products option', () => {
    cy.get('mat-icon').click();
    cy.contains('a', 'products').click();
    });
    it('selects Test, a product', () => {
    cy.contains('Pro4').click();
    });
    it('updates MSRP', () => {
    cy.get("[type='msrp_forTest']").clear();
    cy.get("[type='msrp_forTest']").type('70.99');
    });
    it('clicks the save button', () => {
    cy.get('button').contains('Save').click();
    });
    it('confirms update', () => {
    cy.contains('updated!');
    });
    });