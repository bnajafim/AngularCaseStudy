package com.info5059.casestudy.purchaseOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.info5059.casestudy.product.Product;
import com.info5059.casestudy.product.ProductRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Component

public class PurchaseOrderDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductRepository prodRepo;

    @Transactional
    public PurchaseOrder create(PurchaseOrder purRep) {
        PurchaseOrder realReport = new PurchaseOrder();
        // ProductRepository prodRepo;
        realReport.setPodate(LocalDateTime.now());
        realReport.setVendorid(purRep.getVendorid());
        realReport.setAmount(purRep.getAmount());
        realReport.setPodate(LocalDateTime.now());

        entityManager.persist(realReport);
        // ProductRepository prodRepo=new ProductRepository();

        for (PurchaseOrderLineitem item : purRep.getItems()) {
            PurchaseOrderLineitem realItem = new PurchaseOrderLineitem();

            realItem.setPoid(realReport.getId());
            realItem.setProductid(item.getProductid());
            realItem.setQty(item.getQty());
            realItem.setPrice(item.getPrice());

            Product prod = prodRepo.getReferenceById(item.getProductid());
            prod.setQoo(prod.getQoo() + item.getQty());
            prodRepo.saveAndFlush(prod);

            entityManager.persist(realItem);

        }
        entityManager.refresh(realReport);
        return realReport;

    }

}