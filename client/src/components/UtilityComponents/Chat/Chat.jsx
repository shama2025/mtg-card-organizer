import React, { use } from 'react'
import { Bars } from 'react-loader-spinner'

export default function Chat({modelChat, userChat}) {
  return (
    <div className='p-2'>
        <div className='flex flex-col'>
            {/* User Chat */}
            <div className='flex justify-end items-end'>
            <div className='border w-fit p-1 rounded-md'>
            <p>
                {userChat}
            </p>
            </div>
            </div>
        
        {/* AI Chat */}
          <div className='flex justify-start items-start'>
            <div className='border w-fit p-1 rounded-md'>
                {
                    modelChat?(
                        <p className='wrap-break-word'>{modelChat}</p>
                    ):<Bars/>
                }
            </div>
            </div>
        </div>
        
    </div>
  )
}
