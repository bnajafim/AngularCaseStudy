describe('product add test', () => {
    it('visits the root', () => {
    cy.visit('/');
    });
    it('clicks the menu button products option', () => {
    cy.get('mat-icon').click();
    cy.contains('a', 'products').click();
    });
    it('clicks add icon', () => {
    cy.contains('control_point').click();
    });
    it('fills in fields', () => {
    cy.get('input[formcontrolname=id]').clear();   
    cy.get('input[formcontrolname=id]').type('3B');    
    cy.get('mat-select[formcontrolname="vendorid"]').click(); // open the list
    //cy.get('mat-option').should('have.length.gt', -1); // wait for options
    cy.contains('Shady Sams').click();
    //cy.get('mat-option').contains('Shady Sams').click();    
    cy.get('input[formcontrolname=name]').clear();
    cy.get('input[formcontrolname=name]').type('Pro4');
    cy.get('input[formcontrolname=costprice]').clear();
    cy.get('input[formcontrolname=costprice]').type('50.90');
    cy.get('input[formcontrolname=msrp]').clear();
    cy.get('input[formcontrolname=msrp]').type('60.90');
    cy.get('.mat-expansion-indicator').eq(0).click();
    cy.get('.mat-expansion-indicator').eq(1).click();
    cy.get('input[formcontrolname=rop]').clear();
    cy.get('input[formcontrolname=rop]').type('10');
    cy.get('input[formcontrolname=eoq]').clear();
    cy.get('input[formcontrolname=eoq]').type('50');
    cy.get('input[formcontrolname=qoh]').clear();
    cy.get('input[formcontrolname=qoh]').type('10');
    cy.get('input[formcontrolname=qoo]').clear();
    cy.get('input[formcontrolname=qoo]').type('20');
    });
    it('clicks the save button', () => {
    cy.get('button').contains('Save').click();
    //cy.wait(500);
    });
    it('confirms add', () => {
    cy.contains('added!');
    });
    });