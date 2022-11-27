package com.info5059.casestudy.purchaseOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import com.info5059.casestudy.product.ProductRepository;

@CrossOrigin

@RestController
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderDAO purchOrdertDAO;

    // @Autowired
    // private ProductRepository prodRep;

    @Autowired
    private PurchaseOrderRepository poRepository;

    @PostMapping("/api/pos")
    public ResponseEntity<PurchaseOrder> addOne(@RequestBody PurchaseOrder clientrep) {
        // use RequestBody here

        return new ResponseEntity<PurchaseOrder>(purchOrdertDAO.create(clientrep), HttpStatus.OK);
    }

    @GetMapping("/api/pos")
    public ResponseEntity<Iterable<PurchaseOrder>> findAll() {
        Iterable<PurchaseOrder> pos = poRepository.findAll();
        return new ResponseEntity<Iterable<PurchaseOrder>>(pos, HttpStatus.OK);
    }

    @GetMapping("/api/pos/{id}")
    public ResponseEntity<Iterable<PurchaseOrder>> findByEmployee(@PathVariable Long id) { // use RequestBody here
        return new ResponseEntity<Iterable<PurchaseOrder>>(poRepository.findByVendorid(id),
                HttpStatus.OK);
    }

}
