import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, FormBuilder } from '@angular/forms';
import { Observable, Subscription } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Vendor } from '@app/vendor/vendor';
import { Product } from '@app/product/product';
import { OrderItem } from '@app/order/order-item';
import { Order } from '@app/order/order';
import { VendorService } from '@app/vendor/vendor.service';
import { ProductService } from '@app/product/product.service';
import { OrderService } from '@app/order/order.service';
import { PDFURL } from '@app/constants';

@Component({
  //selector: 'app-generator',
  templateUrl: './generator.component.html',
  //styles: [  ]
})
export class GeneratorComponent implements OnInit, OnDestroy {
  // form
generatorForm: FormGroup;
vendorid: FormControl;
productid: FormControl;
qty: FormControl;

// data
formSubscription?: Subscription;
products$?: Observable<Product[]>; 
vendors$?: Observable<Vendor[]>; // all vendors
vendorproducts$?: Observable<Product[]>; // all products for a particular vendor
items: Array<OrderItem>; // order items that will be in order
qtyOption:Array<Number>

selectedproducts: Product[]; // products that being displayed currently in app
selectedProduct: Product; // the current selected product
selectedVendor: Vendor; // the current selected employee
//selectedQuantities: Array<Number>;

// misc
pickedProduct: boolean;
pickedVendor: boolean;
pickedQty: boolean;
generated: boolean;
hasProducts: boolean;
msg: string;
total: number;
orderno: number = 0;
tax: number = 0.13;
extWithTax: number;
ext: number;
orderid: number = 0;
resetNumber: number;
selectedQty: number;

  constructor(
    private builder: FormBuilder,
    private vendorService: VendorService,
    private productService: ProductService,
    private orderService: OrderService) {

    this.pickedVendor = false;
    this.pickedProduct = false;
    this.pickedQty = false;
    this.generated = false;
    this.msg = '';
    this.vendorid = new FormControl('');
    this.productid = new FormControl('');
    this.qty = new FormControl('');

    this.generatorForm = this.builder.group({
    productid: this.productid,
    qty: this.qty,
    vendorid: this.vendorid,
    });
    this.selectedProduct = {
      id: '',
      vendorid: 0,      
      name: '',
      costprice: 0.0,
      msrp: 0.0,
      rop:0,      
      eoq:0,
      qoh:0,
      qoo:0,
      qrcode:'',
      qrcodetxt:''      
      };

      this.selectedVendor = {
        id: 0,
       name: '',
        address1: '',
        city: '',
        province: '',
        postalcode: '',
        phone: '',
        type: '',
        email: '',
        };
        this.items = new Array<OrderItem>();
        this.selectedproducts = new Array<Product>();
        this.hasProducts = false;
        this.total = 0.0;
        this.extWithTax = 0.0;
        this.ext = 0.0;        
        this.resetNumber = 0;       
        this.selectedQty=0,
        this.qtyOption=[0,1,2,3,4,5];

     }

  ngOnInit(): void {
    this.onPickVendor();
    this.onPickProduct();
    this.onPickQty();
    this.msg = 'loading vendors and products from server...';
    (this.vendors$ = this.vendorService.get()),
    catchError((err) => (this.msg = err.message));
    (this.products$ = this.productService.get()),
    catchError((err) => (this.msg = err.message));
    this.msg = 'server data loaded';
  }// ngOnInit

  ngOnDestroy(): void {
    if (this.formSubscription !== undefined) {
    this.formSubscription.unsubscribe();
    }
    } // ngOnDestroy

  /**
* onPickEmployee - Another way to use Observables, subscribe to the select change event
* then load specific employee expenses for subsequent selection
*/
onPickVendor(): void {
  this.formSubscription = this.generatorForm
  .get('vendorid')
  ?.valueChanges.subscribe((val) => {
  this.selectedProduct = {
  id:'',
  vendorid: 0,       
  name: '',
  costprice: 0.0,
  msrp: 0.0,
  rop:0,      
  eoq:0,
  qoh:0,
  qoo:0, 
  qrcode:'',
  qrcodetxt:''
  };
  this.selectedVendor = val;
  this.loadVendorProducts();
  this.pickedProduct = false;
  this.pickedQty = false;
  this.hasProducts= false;
  this.msg = 'choose product for vendor';
  this.pickedVendor = true;
  this.generated = false;
  this.items = []; // array for the order
  this.selectedproducts = []; // array for the details in app html
  });
  } // onPickVendor
  
  /**
* onPickProduct - subscribe to the select change event then
* update array containing items.
*/
onPickProduct(): void {
  const productSubscription = this.generatorForm
  .get('productid')
  ?.valueChanges.subscribe((val) => {
  this.selectedProduct = val;
  const item: OrderItem = {
  id: 0,
  orderid: 0,
  productid: this.selectedProduct?.id,
  qty:val.eoq,//Behnaz
  price:val.costprice*val.eoq,
  };
  if (
  this.items.find((item) => item.productid === this.selectedProduct?.id)
  ) {
   
  // ignore entry
  } else {
    this.msg=`${this.selectedProduct.eoq} ${this.selectedProduct.name} added!`;
  // add entry
  this.items.push(item);
  this.selectedproducts.push(this.selectedProduct);
  this.pickedProduct = true;
  }
  
  if (this.items.length > 0) {
  this.hasProducts = true;
  }
  this.ext = 0.0;
  this.items.forEach((pro) => (this.ext += pro.price));  
  this.extWithTax = this.ext * this.tax;
   this.total = this.ext + this.extWithTax;
  });
  this.formSubscription?.add(productSubscription); // add it as a child, so all can be destroyed together
 
  } // onPickProduct

  /**
* loadVendorProducts - filter for a particular vendor's products
*/
loadVendorProducts(): void {
  this.vendorproducts$ = this.products$?.pipe(
  map((products) =>
  // map each expense in the array and check whether or not it belongs to selected employee
  products.filter(
  (product) => product.vendorid === this.selectedVendor?.id
  )
  )
  );
   //this.pickedProduct = true;
  } // loadVendorProducts

  onPickQty(): void {
  const qtySubscription = this.generatorForm
  .get('qty')
  ?.valueChanges.subscribe((val) => {
  //this.selectedQty = val;
  if (val === 'EOQ') {
    this.items.forEach((i) => {
      if (i.productid === this.selectedProduct?.id) {
        i.qty = this.selectedProduct.eoq;
        i.price= this.selectedProduct.costprice * i.qty;
        this.pickedQty = true;
      }
    });
    this.msg = `${this.selectedProduct.eoq} ${this.selectedProduct.name} added`;
  }
  else if(val==0){
    this.items = this.items.filter(
      (i) => i.productid !== this.selectedProduct?.id
    );
    this.selectedproducts = this.selectedproducts.filter(
      (i) => i.id !== this.selectedProduct?.id
    );
    this.msg = `All ${this.selectedProduct.name} removed!`;

  }
  else {
    this.items.map((i) => {
      if (i.productid === this.selectedProduct?.id) {
        i.qty = val;
        i.price= this.selectedProduct.costprice * i.qty;
        this.pickedQty = true;
      }
    });
    this.msg = `${val} ${this.selectedProduct.name} added!`;
  }
  if (this.items.length === 0) {
    this.hasProducts = false;
    this.pickedProduct = false;
    this.pickedQty= false;
    this.msg = `No Items`;
  }

  this.ext= 0.0;
  this.items?.forEach((i) => {
    this.ext += i.price;
  });
  this.extWithTax = this.ext * this.tax;
  this.total = this.ext+ this.extWithTax;
});

this.formSubscription?.add(qtySubscription);


  // const item: OrderItem = {
  // id: 0,
  // orderid: 0,
  // productid: this.selectedProduct?.id,
  // qty: val
  // };
  
  //this.pickedQty=true;
  } // onPickQty

  /**
* createOrder - create the client side order
*/
createOrder(): void {
  this.generated = false;
  const order: Order = {
  id: 0,
  items: this.items,
  vendorid: this.selectedProduct.vendorid,
  amount:this.total,
  };
  this.orderService.add(order).subscribe({
  // observer object
  next: (order: Order) => {
  // server should be returning order with new id
  order.id > 0
  ? (this.msg = `Order ${order.id} added!`)
  : (this.msg = 'Order not added! - server error');
  this.orderno = order.id;
  },
  error: (err: Error) => (this.msg = `Order not added! - ${err.message}`),
  complete: () => {
  this.hasProducts = false;
  this.pickedVendor = false;
  this.pickedProduct = false;
  this.pickedQty = false;
  this.generated = true;
  },
  });
  } // createOrder
  viewPdf(): void {
    window.open(`${PDFURL}${this.orderno}`, '');
    } // viewPdf

}
