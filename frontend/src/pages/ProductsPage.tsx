import React, { useState } from 'react';
import ProductList from '@/components/ProductList/ProductList';
import './ProductsPage.css';

const ProductsPage: React.FC = () => {
  const [showLowStockOnly, setShowLowStockOnly] = useState(false);

  return (
    <div className="products-page">
      <header className="products-header">
        <h1>Product Management</h1>
        <div className="filter-controls">
          <label>
            <input
              type="checkbox"
              checked={showLowStockOnly}
              onChange={(e) => setShowLowStockOnly(e.target.checked)}
            />
            Show only low stock products
          </label>
        </div>
      </header>
      
      <ProductList showLowStockOnly={showLowStockOnly} />
    </div>
  );
};

export default ProductsPage;