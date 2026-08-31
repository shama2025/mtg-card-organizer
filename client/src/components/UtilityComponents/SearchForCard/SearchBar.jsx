import React from 'react'
import { useState } from 'react'

export default function SearchBar({searchQuery, setSearchQuery}) {
  return (
    <div className='pb-4'>
        <input type='text' 
        placeholder='Search for a card you own.'
        className='border p-1 rounded-md pb-1'
        name='searchQuery'
        value={searchQuery}
        onChange={(event) => setSearchQuery(event.target.value)}
        />
    </div>
  )
}
